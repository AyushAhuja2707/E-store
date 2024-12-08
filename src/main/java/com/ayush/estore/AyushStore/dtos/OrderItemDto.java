package com.ayush.estore.AyushStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {
    private int orderItemId;

    private int quantity;

    private int totalPrice;

    private ProductDto product;
}
