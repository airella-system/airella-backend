package pl.edu.agh.airsystem.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.airsystem.model.Client;
import pl.edu.agh.airsystem.model.StationClient;
import pl.edu.agh.airsystem.model.UserClient;
import pl.edu.agh.airsystem.model.auth.LoginRequest;
import pl.edu.agh.airsystem.model.auth.LoginResponse;
import pl.edu.agh.airsystem.model.auth.RefreshTokenRequest;
import pl.edu.agh.airsystem.model.auth.RefreshTokenResponse;
import pl.edu.agh.airsystem.repository.ClientRepository;
import pl.edu.agh.airsystem.repository.UserClientRepository;
import pl.edu.agh.airsystem.security.JWTTokenUtil;
import pl.edu.agh.airsystem.security.model.JWTToken;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class Auth {

    private final AuthenticationManager authenticationManager;
    private final JWTTokenUtil jwtTokenUtil;
    private final ClientRepository clientRepository;
    private final UserClientRepository userClientRepository;

    @GetMapping("/test")
    public String test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserClient) {
            return "You are user";
        } else if (principal instanceof StationClient) {
            return "You are station";
        }
        return "You are... I don't know ;(";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserClient userClient = userClientRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + authenticationRequest.getUsername() + " not found"));

        final JWTToken accessToken = jwtTokenUtil.generateAccessToken(userClient);
        final String refreshToken = userClient.getRefreshToken();

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest authenticationRequest) {
        String refreshToken = authenticationRequest.getRefreshToken();
        System.out.println("SADDS " + refreshToken);

        Client client = clientRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username not found"));
        System.out.println("SADDS2 " + refreshToken);

        final JWTToken token = jwtTokenUtil.generateAccessToken(client);
        return ResponseEntity.ok(new RefreshTokenResponse(token));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}