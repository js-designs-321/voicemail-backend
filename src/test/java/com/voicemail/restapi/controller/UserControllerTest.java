package com.voicemail.restapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.voicemail.restapi.service.UserAuthenticationService;
import com.voicemail.restapi.util.AuthenticationResponse;
import com.voicemail.restapi.util.LoginRequest;
import com.voicemail.restapi.util.RegisterRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerTest {

    @Mock
    private UserAuthenticationService userAuthenticationService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userAuthenticationService);
    }

    @Test
    public void registerUserTest() {
        RegisterRequest registerRequest = new RegisterRequest("test","user","testuser@voicemail.com", "password");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("token", "refreshToken");
        when(userAuthenticationService.register(any(RegisterRequest.class))).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> responseEntity = userController.register(registerRequest);

        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() == authenticationResponse;
    }

    @Test
    public void loginUserTest() {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("token", "refreshToken");
        when(userAuthenticationService.login(any(LoginRequest.class))).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> responseEntity = userController.login(loginRequest);

        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody() == authenticationResponse;
    }

    @Test
    public void refreshTokenTest() throws IOException {
        doNothing().when(userAuthenticationService).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));

        userController.refreshToken(request, response);
    }
}
