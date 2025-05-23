package com.user.service;

import com.user.model.dto.request.UserRequest;
import com.user.model.dto.request.UserUpdateRequest;
import com.user.model.dto.response.UserResponse;
import com.user.exception.ResourceNotFoundException;
import com.user.model.dto.response.UserResponseAuth;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<UserResponse> getAllUsers();

    Optional<UserResponse> getUser(Long id) throws ResourceNotFoundException;

    UserResponseAuth getUserByEmail(String email) throws ResourceNotFoundException;

    Optional<UserResponse> updateUser(Long id, UserUpdateRequest request) throws ResourceNotFoundException;

    UserResponse createUser(UserRequest request);

    void deleteUser(Long id) throws ResourceNotFoundException;

}
