package com.ayush.estore.AyushStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {
    private String token;
    UserDtos userDtos;
    private String refreshtoken;
}
