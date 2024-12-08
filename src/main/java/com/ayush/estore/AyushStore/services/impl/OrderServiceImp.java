package com.ayush.estore.AyushStore.services.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ayush.estore.AyushStore.Repository.CartItemRepository;
import com.ayush.estore.AyushStore.Repository.CartRepo;
import com.ayush.estore.AyushStore.Repository.OrderRepo;
import com.ayush.estore.AyushStore.Repository.UserRepo;
import com.ayush.estore.AyushStore.dtos.CreateOrderRequest;
import com.ayush.estore.AyushStore.dtos.OrderDto;
import com.ayush.estore.AyushStore.dtos.PageableResponse;
import com.ayush.estore.AyushStore.entities.Cart;
import com.ayush.estore.AyushStore.entities.CartItem;
import com.ayush.estore.AyushStore.entities.Order;
import com.ayush.estore.AyushStore.entities.OrderItem;
import com.ayush.estore.AyushStore.entities.User;
import com.ayush.estore.AyushStore.exceptions.BadApiRequestException;
import com.ayush.estore.AyushStore.exceptions.ResourceNotFoundException;
import com.ayush.estore.AyushStore.helper.Helper;
import com.ayush.estore.AyushStore.services.OrderService;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    // @Override
    // public OrderDto createOrder(CreateOrderRequest orderDto) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'createOrder'");
    // }

    @Override
    // public OrderDto createOrder(OrderDto orderDto, String userId, String cartId)
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'createOrder'");
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
        Cart cart = cartRepository.findById(orderDto.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart with given id not found on server !!"));

        List<CartItem> cartItems = cart.getItems();

        if (cartItems.size() <= 0) {
            throw new BadApiRequestException("Invalid number of items in cart !!");
        }
        // other checks
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            // CartItem->OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        //

        cart.getItems().clear();

        cartRepository.save(cart);
        cartItemRepository.delete(modelMapper.map(cart, CartItem.class));
        cartRepository.delete(cart);
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);

    }

    @Override
    public void removeOrder(String orderId) {
        // TODO Auto-generated method stub
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order is not found !!"));
        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        // TODO Auto-generated method stub
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // TODO Auto-generated method stub
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(page, OrderDto.class);
    }

}
