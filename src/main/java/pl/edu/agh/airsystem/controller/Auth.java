package pl.edu.agh.airsystem.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.airsystem.model.ApplicationUser;
import pl.edu.agh.airsystem.model.auth.LoginRequest;
import pl.edu.agh.airsystem.model.auth.LoginResponse;
import pl.edu.agh.airsystem.model.auth.RefreshTokenRequest;
import pl.edu.agh.airsystem.model.auth.RefreshTokenResponse;
import pl.edu.agh.airsystem.repository.UserRepository;
import pl.edu.agh.airsystem.security.JWTTokenUtil;
import pl.edu.agh.airsystem.security.model.JWTToken;
import pl.edu.agh.airsystem.service.CustomUserDetailsService;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class Auth {

    private final AuthenticationManager authenticationManager;
    private final JWTTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        ApplicationUser user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + authenticationRequest.getUsername() + " not found"));

        // create new refresh token if not exists
        if (user.getRefreshToken() == null) {
            user.setRefreshToken(UUID.randomUUID().toString());
            userRepository.save(user);
        }

        final JWTToken accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        final String refreshToken = user.getRefreshToken();

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String refreshToken = authenticationRequest.getRefreshToken();

        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username " + username + " not found"));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (refreshToken.equals(user.getRefreshToken())) {
            final JWTToken token = jwtTokenUtil.generateAccessToken(userDetails);
            return ResponseEntity.ok(new RefreshTokenResponse(token));
        }

        return new ResponseEntity<Object>("Access denied",
                new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
    }
}