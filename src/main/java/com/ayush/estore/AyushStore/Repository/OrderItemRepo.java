package com.ayush.estore.AyushStore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.estore.AyushStore.entities.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

}
