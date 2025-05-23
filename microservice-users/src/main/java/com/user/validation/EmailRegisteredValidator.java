package com.user.validation;

import com.user.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailRegisteredValidator implements ConstraintValidator<EmailRegistered,String> {

    private final IUserRepository repository;
    public EmailRegisteredValidator(IUserRepository repository){
        this.repository = repository;
    }


    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        if (email == null || email.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email cannot be empty or null").addConstraintViolation();
            return false;
        }
       if(repository.existsByEmail(email)){
           context.disableDefaultConstraintViolation();
           context.buildConstraintViolationWithTemplate("Email already exists ").addConstraintViolation();
           return false;
       }
       return true;
    }
}
