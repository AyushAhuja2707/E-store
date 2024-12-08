package com.ayush.estore.AyushStore.services.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ayush.estore.AyushStore.Repository.CartItemRepository;
import com.ayush.estore.AyushStore.Repository.CartRepo;
import com.ayush.estore.AyushStore.Repository.ProductRepo;
import com.ayush.estore.AyushStore.Repository.UserRepo;
import com.ayush.estore.AyushStore.dtos.AddItemToCartRequest;
import com.ayush.estore.AyushStore.dtos.CartDto;
import com.ayush.estore.AyushStore.entities.Cart;
import com.ayush.estore.AyushStore.entities.CartItem;
import com.ayush.estore.AyushStore.entities.Product;
import com.ayush.estore.AyushStore.entities.User;
import com.ayush.estore.AyushStore.exceptions.BadApiRequestException;
import com.ayush.estore.AyushStore.exceptions.ResourceNotFoundException;
import com.ayush.estore.AyushStore.services.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequestException("Requested quantity is not valid !!");
        }

        // fetch the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));
        // fetch the user from db
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));

        Cart cart = null;
        if (product.getQuantity() < request.getQuantity()) {
            throw new BadApiRequestException(
                    "Please check your  added quantity once should be less than " + product.getQuantity());
        }

        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        // perform cart operations
        // if cart items already present; then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                // item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
                product.setQuantity(product.getQuantity() - quantity);
            }
            return item;
        }).collect(Collectors.toList());

        // cart.setItems(updatedItems);

        // create items
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);

            product.setQuantity(product.getQuantity() - quantity);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);

    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        // conditions

        CartItem cartItem1 = cartItemRepository.findById(cartItem)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item not found !!"));

        // cartItem1.getQuantity();
        System.out.println("Check AYUSH" + cartItem1.getProduct());
        Product product = productRepository.findById(cartItem1.getProduct().getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));
        product.setQuantity(cartItem1.getProduct().getQuantity() + cartItem1.getQuantity());
        cartItemRepository.delete(cartItem1);

    }

    @Override
    public void clearCart(String userId) {
        // fetch the user from db
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
        for (CartItem c : cart.getItems()) {
            Product product = productRepository.findById(c.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found in database !!"));
            product.setQuantity(c.getProduct().getQuantity() + c.getQuantity());

        }
        cart.getItems().clear();

        cartRepository.save(cart);
        cartRepository.delete(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
        return mapper.map(cart, CartDto.class);
    }
}
