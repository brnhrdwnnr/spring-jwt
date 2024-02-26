package com.bernhard.springJwt.service;

import com.bernhard.springJwt.dto.UserLoginRequest;
import com.bernhard.springJwt.dto.UserRegisterRequest;
import com.bernhard.springJwt.dto.AuthenticationResponse;
import com.bernhard.springJwt.entity.Token;
import com.bernhard.springJwt.entity.User;
import com.bernhard.springJwt.repository.TokenRepository;
import com.bernhard.springJwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(UserRegisterRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        user = repository.save(user);
        String token = jwtService.generateToken(user);

        saveUserToken(token, user);

        return new AuthenticationResponse(token);

    }

    private void saveUserToken(String jwtToken, User user) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        revokeAllTokenByUser(user);

        saveUserToken(token, user);

        return new AuthenticationResponse(token);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());
        if (!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach( t-> {
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokenListByUser);
    }
}
