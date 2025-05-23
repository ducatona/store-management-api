package com.user.service.impl;

import com.user.model.dto.request.UserRequest;
import com.user.model.dto.request.UserUpdateRequest;
import com.user.model.dto.response.UserResponse;
import com.user.exception.ResourceNotFoundException;
import com.user.repository.User;
import com.user.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @Mock
    private IUserRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    private List<UserResponse> listOfUsers;
    private User user;
    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;
    private User userUpdated;
    private UserResponse userResponseUpdated;
    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("Adrian");
        user.setEmail("adrian@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        userResponse = new UserResponse(1l, "Adrian", "adrian@gmail.com","ROLE_USER");

        listOfUsers = new ArrayList<>();
        listOfUsers.add(userResponse);

        userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setName("Pepe");
        userUpdateRequest.setEmail("pepe@gmail.com");
        userUpdated = new User();
        userUpdated.setName("Pepe");
        userUpdated.setEmail("pepe@gmail.com");

        userResponseUpdated = new UserResponse();
        userResponseUpdated.setName("Pepe");
        userResponseUpdated.setEmail("pepe@gmail.com");

    }


    @Test
    void getAllUsers_whenExitsUsers_returnListOfUsers() {

        int expected = 2;

        Mockito.when(repository.findAll()).thenReturn(List.of(user));
        Mockito.when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

        List<UserResponse> actual = service.getAllUsers();

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(1l, actual.get(0).getId());
        Assertions.assertEquals("Adrian", actual.get(0).getName());
        Assertions.assertEquals("adrian@gmail.com", actual.get(0).getEmail());

    }

    @Test
    void getUserById_whenGivenExistingId_returnUserDetails() throws ResourceNotFoundException {

        Long idUser = 1l;

        Mockito.when(repository.findById(idUser)).thenReturn(Optional.ofNullable(user));
        Mockito.when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

        Optional<UserResponse> actual = service.getUser(idUser);

        Assertions.assertEquals("Adrian", actual.get().getName());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals("adrian@gmail.com", actual.get().getEmail());

    }


    @Test
    void getUserById_whenGiveInExistingId_throwNotFoundError() throws ResourceNotFoundException {


        Long idUser = 8L;

        Mockito.when(repository.findById(idUser)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getUser(idUser));

        Assertions.assertEquals("User not found with id: " + idUser, exception.getMessage());

    }

    @Test
    void updateUser_whenUserExists_returnObjectUpdated() throws ResourceNotFoundException {

        Long idUser = 1l;

        Mockito.when(repository.findById(idUser)).thenReturn(Optional.ofNullable(user));
        Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(userUpdated);
        Mockito.when(modelMapper.map(Mockito.any(Optional.class),Mockito.eq(UserResponse.class))).thenReturn(userResponseUpdated);


        Optional<UserResponse> actual = service.updateUser(idUser,userUpdateRequest);

        Assertions.assertEquals("Pepe",actual.get().getName());
        Assertions.assertEquals("pepe@gmail.com",actual.get().getEmail());

    }

    @Test
    void updateUser_whenUserNotExists_throwUserNotFoundException(){

        Long idUser = 2l;

        Mockito.when(repository.findById(idUser)).thenReturn(Optional.empty());

        ResourceNotFoundException actual = Assertions.assertThrows(ResourceNotFoundException.class,() -> service.updateUser(idUser,userUpdateRequest));

        Assertions.assertEquals("User not found with id: " +idUser ,actual.getMessage());


    }


    @Test
    void createUser_whenUserCreates_returnUserObject(){

    userRequest = new UserRequest();
    userRequest.setName("Adrian");
    userRequest.setEmail("adrian@gmail.com");
    userRequest.setPassword(passwordEncoder.encode("1234"));


    Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(user);
    Mockito.when(modelMapper.map(Mockito.any(User.class),Mockito.eq(UserResponse.class))).thenReturn(userResponse);

    UserResponse actual =service.createUser(userRequest);

    Assertions.assertEquals("Adrian",actual.getName());


    }












    @Test
    void deleteUser_whenGiveExistingIdUser_deleteUser() throws ResourceNotFoundException {

        Long idUser = 1L;


        Mockito.when(repository.findById(idUser)).thenReturn(Optional.of(user));

        service.deleteUser(idUser);

        Mockito.verify(repository).deleteById(idUser);



    }

    @Test
    void deleteUser_whenGiveNotExistingIdUser_deleteUser() throws ResourceNotFoundException {

        Long idUser = 2L;
        Mockito.when(repository.findById(idUser)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,()->service.deleteUser(idUser));


        Assertions.assertEquals("The user cannot be deleted with the id:" +idUser,exception.getMessage());


    }
    }




