package com.ayush.estore.AyushStore.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String categoryId;

    @NotBlank
    @Size(min = 4, message = "title must be of min 4 characters")
    private String title;

    @NotBlank(message = "Description required")
    private String description;

    @NotBlank(message = "coverimage required")
    private String coverImage;
}
