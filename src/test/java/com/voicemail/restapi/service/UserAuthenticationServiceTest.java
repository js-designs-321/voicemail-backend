package com.voicemail.restapi.service;

import com.voicemail.restapi.enums.Role;
import com.voicemail.restapi.enums.TokenType;
import com.voicemail.restapi.model.Token;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.TokenRepository;
import com.voicemail.restapi.repository.UserRepository;
import com.voicemail.restapi.util.AuthenticationResponse;
import com.voicemail.restapi.util.LoginRequest;
import com.voicemail.restapi.util.RegisterRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserAuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserAuthenticationService userAuthenticationService;

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password");

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        String jwtToken = "jwt_token";
        String refreshToken = "refresh_token";

        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

        AuthenticationResponse actualResponse = userAuthenticationService.register(request);

        assertEquals(expectedResponse.getAccessToken(), actualResponse.getAccessToken());
        assertEquals(expectedResponse.getRefreshToken(), actualResponse.getRefreshToken());

        verify(userRepository, times(1)).save(Mockito.any(User.class));
        verify(jwtService, times(1)).generateToken(user);
        verify(jwtService, times(1)).generateRefreshToken(user);
        verify(tokenRepository, times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john.doe@example.com");
        request.setPassword("password");

        User user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        String jwtToken = "jwt_token";
        String refreshToken = "refresh_token";

        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        List<Token> tokens = new ArrayList<>();
        Token existingToken = Token.builder()
                .user(user)
                .token("existing_token")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokens.add(existingToken);

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(tokens);

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

        AuthenticationResponse actualResponse = userAuthenticationService.login(request);

        assertEquals(expectedResponse.getAccessToken(), actualResponse.getAccessToken());
        assertEquals(expectedResponse.getRefreshToken(), actualResponse.getRefreshToken());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(jwtService, times(1)).generateToken(user);
        verify(jwtService, times(1)).generateRefreshToken(user);
        verify(tokenRepository, times(1)).findAllValidTokenByUser(user.getId());
        verify(tokenRepository, times(1)).saveAll(tokens);
    }

}
