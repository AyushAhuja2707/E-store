package com.ayush.estore.AyushStore.dtos;

import java.util.Date;

import com.ayush.estore.AyushStore.entities.Catergory;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {
    private String productId;

    private String title;

    private String description;

    private int price;

    private int discountedPrice;

    private int quantity;

    private Date addedDate;

    private boolean isLive;

    private boolean stock;

    private String productImageName;

    private CategoryDto catergory;

}
