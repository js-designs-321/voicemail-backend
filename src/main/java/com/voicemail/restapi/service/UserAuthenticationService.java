package com.voicemail.restapi.service;

import com.voicemail.restapi.enums.Role;
import com.voicemail.restapi.model.User;
import com.voicemail.restapi.repository.UserRepository;
import com.voicemail.restapi.util.AuthenticationResponse;
import com.voicemail.restapi.util.LoginRequest;
import com.voicemail.restapi.util.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
         userRepository.save(user);
         var jwtToken = jwtService.generateToken(user);
         return AuthenticationResponse.builder()
                 .token(jwtToken)
                 .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         request.getEmail(),
                         request.getPassword()
                 )
         );
         var user = userRepository.findByEmail(request.getEmail())
                 .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


}
