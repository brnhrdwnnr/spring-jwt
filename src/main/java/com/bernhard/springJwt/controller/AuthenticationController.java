package com.bernhard.springJwt.controller;

import com.bernhard.springJwt.dto.UserLoginRequest;
import com.bernhard.springJwt.dto.UserRegisterRequest;
import com.bernhard.springJwt.dto.AuthenticationResponse;
import com.bernhard.springJwt.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserRegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}
