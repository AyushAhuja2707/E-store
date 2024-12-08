package com.ayush.estore.AyushStore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayush.estore.AyushStore.entities.Cart;
import com.ayush.estore.AyushStore.entities.User;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, String> {

    Optional<Cart> findByUser(User user);

}
