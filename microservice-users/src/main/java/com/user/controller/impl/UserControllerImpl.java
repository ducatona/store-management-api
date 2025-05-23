package com.user.controller.impl;

import com.user.controller.IUserController;
import com.user.model.dto.request.UserRequest;
import com.user.model.dto.request.UserUpdateRequest;
import com.user.model.dto.response.UserResponse;
import com.user.exception.ResourceNotFoundException;
import com.user.model.dto.response.UserResponseAuth;
import com.user.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserControllerImpl implements IUserController {


    private final UserServiceImpl service;

    public UserControllerImpl(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
    return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserResponse>> getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(service.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid UserRequest request) {
       return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<UserResponse>> updateUser(@PathVariable Long id, @Valid UserUpdateRequest request) throws ResourceNotFoundException {
       return ResponseEntity.ok(service.updateUser(id,request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) throws ResourceNotFoundException {
        service.deleteUser(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseAuth> getUserByEmail(@PathVariable String email) throws ResourceNotFoundException {
        return ResponseEntity.ok(service.getUserByEmail(email));
    }


}
