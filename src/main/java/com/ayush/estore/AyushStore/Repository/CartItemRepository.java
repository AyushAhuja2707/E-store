package com.ayush.estore.AyushStore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.estore.AyushStore.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
