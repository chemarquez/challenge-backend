package com.codingchallenge.spotify.controller;

import com.codingchallenge.spotify.security.AuthResponse;
import com.codingchallenge.spotify.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.codingchallenge.spotify.security.SecurityConstants.MOCK_PASSWORD;
import static com.codingchallenge.spotify.security.SecurityConstants.MOCK_USERNAME;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Operation(summary = "Login and get JWT token", description = "Authenticates the user and returns a JWT token if the credentials are correct.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT token generated",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))),
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<AuthResponse> login(
            @RequestParam String username,
            @RequestParam String password) {

        if (containsRequiredData(username, password))
            return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));

        // Validate credentials (mock validation for simplicity)
        if (MOCK_USERNAME.equals(username) && MOCK_PASSWORD.equals(password)) {
            // Generate a JWT token if credentials are valid
            String token = jwtTokenProvider.generateToken(username);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
        }
    }

    private boolean containsRequiredData(String username, String password) {
        return username != null && !username.isEmpty() && !username.isBlank() &&
                password == null || password.isEmpty() || password.isBlank();
    }
}
