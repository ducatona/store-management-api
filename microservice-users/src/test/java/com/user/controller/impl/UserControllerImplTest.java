package com.user.controller.impl;

import com.user.model.dto.request.UserRequest;
import com.user.model.dto.request.UserUpdateRequest;
import com.user.model.dto.response.UserResponse;
import com.user.exception.ResourceNotFoundException;
import com.user.repository.IUserRepository;
import com.user.security.JwtFilter;
import com.user.security.SecurityConfig;
import com.user.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class UserControllerImplTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockitoBean
    private IUserRepository repository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserServiceImpl service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtFilter).doFilter(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void getAllUsers_whenUsersExists_returnListOfUsers() throws Exception {

        List<UserResponse> listOfUsers = new ArrayList<>();
        listOfUsers.add(new UserResponse(1l, "Adrian", "adrian@gmail.com", "ROLE_USER"));
        listOfUsers.add(new UserResponse(2l, "Pepe", "pepe@gmail.com", "ROLE_ADMIN"));
        listOfUsers.add(new UserResponse(3l, "Ramon", "ramon@gmail.com", "ROLE_USER"));


        Mockito.when(service.getAllUsers()).thenReturn(listOfUsers);

        ResultActions perform = mockMvc.perform(get("/api/v1/user").with(user("admin").roles("ADMIN")));

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1l))
                .andExpect(jsonPath("$[0].name").value("Adrian"))
                .andExpect(jsonPath("$[0].email").value("adrian@gmail.com"));

    }


    @Test
    void getUserById_whenGivenValidId_returnUserDetails() throws Exception {

        long userId = 1l;

        UserResponse response = new UserResponse();
        response.setId(userId);
        response.setName("Adrian");
        response.setEmail("adrian@gmail.com");

        Mockito.when(service.getUser(userId)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/user/{id}", userId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1l))
                .andExpect(jsonPath("$.name").value("Adrian"))
                .andExpect(jsonPath("email").value("adrian@gmail.com"));

    }

    @Test
    void getUserById_whenGivenInvalidId_returnNotFoundException() throws Exception {

        long userId = 9;

        Mockito.when(service.getUser(userId)).thenThrow(new ResourceNotFoundException("User not found with id: +id"));

        mockMvc.perform(get("/api/v1/user/{id}", userId)).andExpect(status().isNotFound());


    }


    @Test
    void createUser_whenGivenCorrectUser_returnUserCreated() throws Exception {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserRequest request = new UserRequest();
        request.setName("adrian");
        request.setEmail("adrian@gmail.com");
        request.setPassword(passwordEncoder.encode("1234"));

        UserResponse response = new UserResponse(1l, "adrian", "adrian@gmail.com", "ROLE_USER");

        Mockito.when(service.createUser(Mockito.any(UserRequest.class))).thenReturn(response);

        String objectToString = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/user").with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1l));

    }

    @Test
    void createUser_whenGivenCorrectUser_throwBadRequestName() throws Exception {

        UserRequest request = new UserRequest();
        request.setName("");
        request.setEmail("adrian@gmail.com");
        request.setPassword(passwordEncoder.encode("1234"));

        String objecTostring = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/api/v1/user").with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).content(objecTostring));

        perform.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name:Cannot be null or empty"));

    }

    @Test
    void updateUser_whenGivenExistingUser_returnUpdatedUser() throws Exception {

        Long idUser = 1l;

        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Adolfo");
        request.setEmail("adolfo@gmail.com");

        UserResponse response = new UserResponse();
        response.setId(idUser);
        response.setName("Adolfo");
        response.setEmail("adolfo@gmail.com");
        Mockito.when(service.updateUser(Mockito.eq(idUser), Mockito.any(UserUpdateRequest.class))).thenReturn(Optional.of(response));

        String objectToString = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/user/{id}", idUser).with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString))
                .andExpect(status().isOk());
    }


    @Test
    void updateUser_whenGiveInExistingUser_throwNotFoundException() throws Exception {

        long idUser = 3L;
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("adrian");
        request.setEmail("adrian@gmail.com");


        Mockito.when(service.updateUser(Mockito.eq(idUser), Mockito.any(UserUpdateRequest.class))).thenThrow(new ResourceNotFoundException("User not found with id: " + idUser));


        String objectToString = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/user/{id}", idUser).with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).content(objectToString)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: " + idUser));


    }

    @Test
    void updateUser_whenUserRequestIsBad_throwBadRequestExceptionId() throws Exception {

        long idUser = 3L;
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("");
        request.setEmail("adrian@gmail.com");


        String objectToString = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/user/{id}", idUser).with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).content(objectToString)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name:Cannot be null or empty"));


    }


    @Test
    void updateUser_whenUserRequestIsBad_throwBadRequestExceptionEmail() throws Exception {

        long idUser = 1L;
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Adrian");
        request.setEmail("agmail.com");


        String objectToString = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/user/{id}", idUser).with(user("admin").roles("ADMIN")).contentType(MediaType.APPLICATION_JSON).content(objectToString)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email:must be a well-formed email address"));

    }

    @Test
    void deleteUser_whenUserNotExists_returnCode404() throws Exception {

        long idUser = 5L;

        Mockito.doThrow(new ResourceNotFoundException("The user cannot be deleted with the id: " + idUser)).when(service).deleteUser(idUser);

        mockMvc.perform(delete("/api/v1/user/{id}", idUser).with(user("admin").roles("ADMIN"))).andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1)).deleteUser(idUser);


    }


    @Test
    void deleteUser_whenUserExists_returnCode200() throws Exception {
        long idUser = 1L;

        Mockito.doNothing().when(service).deleteUser(idUser);

        mockMvc.perform(delete("/api/v1/user/{id}", idUser).with(user("admin").roles("ADMIN"))).andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).deleteUser(idUser);


    }


}