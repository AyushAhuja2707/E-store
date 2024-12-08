package com.ayush.estore.AyushStore.services;

import java.util.List;

import com.ayush.estore.AyushStore.dtos.CreateOrderRequest;
import com.ayush.estore.AyushStore.dtos.OrderDto;
import com.ayush.estore.AyushStore.dtos.PageableResponse;

public interface OrderService {
    // create order
    OrderDto createOrder(CreateOrderRequest orderDto);
    // OrderDto createOrder(OrderDto orderDto, String userId, String cartId);

    // remove order
    void removeOrder(String orderId);

    // get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

    // get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

}
