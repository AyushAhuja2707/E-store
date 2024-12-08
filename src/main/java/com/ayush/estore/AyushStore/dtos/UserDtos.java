package com.ayush.estore.AyushStore.dtos;

import java.util.List;

import org.hibernate.annotations.processing.Pattern;

import com.ayush.estore.AyushStore.entities.Providers;
import com.ayush.estore.AyushStore.entities.Role;
import com.ayush.estore.AyushStore.ownvalidators.ImageName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtos {

    private String userId;

    @Size(min = 3, max = 20, message = "Invalid Name !!")
    private String name;

    // @Email(message = "Invalid User Email !!")
    @jakarta.validation.constraints.Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid User Email !!")
    @NotBlank(message = "Email is required !!")
    private String email;

    @NotBlank(message = "Password is required !!")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    private List<RoleDtos> roles;

    @ImageName // my own Annotation
    private String imageName;

    private Providers provider;

}
