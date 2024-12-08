package com.ayush.estore.AyushStore.ownvalidators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageName, String> {

    private Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'isValid'");
        // logic

        logger.info("Message From isValid {}", value);
        if (value.isBlank()) {
            return false;
        } else
            return true;
        // return false;

    }

}
