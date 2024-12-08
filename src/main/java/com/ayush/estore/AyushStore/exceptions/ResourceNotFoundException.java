package com.ayush.estore.AyushStore.exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource not found");
    }

    public ResourceNotFoundException(String messString) {
        super(messString);
    }

}
