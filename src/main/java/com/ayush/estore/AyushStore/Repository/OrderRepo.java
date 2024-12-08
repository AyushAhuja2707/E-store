package com.ayush.estore.AyushStore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.estore.AyushStore.entities.Order;
import com.ayush.estore.AyushStore.entities.User;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
}
