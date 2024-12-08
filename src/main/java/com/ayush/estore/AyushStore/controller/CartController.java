package com.ayush.estore.AyushStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.estore.AyushStore.config.AppConstants;
import com.ayush.estore.AyushStore.dtos.AddItemToCartRequest;
import com.ayush.estore.AyushStore.dtos.ApiResponseMessage;
import com.ayush.estore.AyushStore.dtos.CartDto;
import com.ayush.estore.AyushStore.services.CartService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/carts")
public class CartController {
    // add items to cart
    @Autowired
    private CartService cartService;

    @PreAuthorize("hasAnyRole('" + AppConstants.ROLE_ADMIN + "','" + AppConstants.ROLE_NORMAL + "')")

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,
            @RequestBody AddItemToCartRequest request) {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('" + AppConstants.ROLE_ADMIN + "','" + AppConstants.ROLE_NORMAL + "')")

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,
            @PathVariable int itemId) {
        cartService.removeItemFromCart(userId, itemId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Item is removed !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Now cart is blank !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}