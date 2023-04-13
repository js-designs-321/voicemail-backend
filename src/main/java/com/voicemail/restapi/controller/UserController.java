package com.voicemail.restapi.controller;

import com.voicemail.restapi.service.UserAuthenticationService;
import com.voicemail.restapi.util.AuthenticationResponse;
import com.voicemail.restapi.util.LoginRequest;
import com.voicemail.restapi.util.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voicemail/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserAuthenticationService userAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(userAuthenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(userAuthenticationService.login(request));
    }

}
