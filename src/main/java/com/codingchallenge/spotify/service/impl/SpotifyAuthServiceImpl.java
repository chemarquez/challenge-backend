package com.codingchallenge.spotify.service.impl;

import com.codingchallenge.spotify.service.SpotifyAuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@Service
public class SpotifyAuthServiceImpl implements SpotifyAuthService {
    private static final Logger logger = LogManager.getLogger(com.codingchallenge.spotify.service.SpotifyAuthService.class);

    @Value("${spotify.client_id}")
    private String clientId;

    @Value("${spotify.client_secret}")
    private String clientSecret;

    @Value("${spotify.token.url}")
    private String tokenUrl;

    public String getAccessToken() {
        // Create authorization header with client_id and client_secret
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64Utils.encodeToString(credentials.getBytes());

        // Set up the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set up the request body
        String body = "grant_type=client_credentials";

        // Create the HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Use RestTemplate to send the POST request
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);
            // Extract the access token from the response
            String accessToken = parseAccessTokenFromResponse(response.getBody());
            return accessToken;
        } catch (HttpClientErrorException e) {
            // Handle error appropriately
            logger.error("--getAccesToken() - ", e);
            throw new RuntimeException("Failed to retrieve access token", e);
        }
    }

    private String parseAccessTokenFromResponse(String responseBody) {
        // Parse the access token from the response body
        // Response is typically in JSON format: {"access_token": "...", "token_type": "..."}
        String accessToken = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            accessToken = jsonNode.get("access_token").asText();
        } catch (IOException e) {
            logger.error("--parseAccessTokenFromResponse() - ", e);
        }
        return accessToken;
    }
}
