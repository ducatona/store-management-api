package com.user.service.impl;

import com.user.model.dto.request.UserRequest;
import com.user.model.dto.request.UserUpdateRequest;
import com.user.model.dto.response.UserResponse;
import com.user.exception.ResourceNotFoundException;
import com.user.model.dto.response.UserResponseAuth;
import com.user.repository.User;
import com.user.repository.IUserRepository;
import com.user.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final ModelMapper modelMapper;
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(ModelMapper modelMapper, IUserRepository repository,PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponse> getUser(Long id) throws ResourceNotFoundException {
        return Optional.ofNullable(repository.findById(id).stream().map(user -> modelMapper.map(user, UserResponse.class)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +id)));
    }

    @Override
    public UserResponseAuth getUserByEmail(String email) throws ResourceNotFoundException {

        User user = repository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with email " + email + " not found")
        );
        return modelMapper.map(user, UserResponseAuth.class);
    }

    @Override
    public Optional<UserResponse> updateUser(Long id, UserUpdateRequest request) throws ResourceNotFoundException {

        Optional<User> userFounded = Optional.ofNullable(repository.findById(id).stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +id)));

            userFounded.ifPresent(product -> {
                product.setName(request.getName());
                product.setEmail(request.getEmail());

                repository.save(product);
            });

        return Optional.of(modelMapper.map(userFounded, UserResponse.class));
    }

    @Override
    public UserResponse createUser(UserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        repository.save(user);

        return modelMapper.map(user, UserResponse.class);

    }

    @Override
    public void deleteUser(Long id) throws ResourceNotFoundException {

        Optional<User> userFound = repository.findById(id);

        if (!userFound.isPresent()) {
            throw new ResourceNotFoundException("The user cannot be deleted with the id:" + id);
        }

        repository.deleteById(id);

    }
}
