package com.codingchallenge.spotify.service.impl;


import com.codingchallenge.spotify.model.Track;
import com.codingchallenge.spotify.service.SpotifyAuthService;
import com.codingchallenge.spotify.service.SpotifyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class SpotifyServiceImpl implements SpotifyService {
    private static final Logger logger = LogManager.getLogger(com.codingchallenge.spotify.service.SpotifyService.class);
    @Value("${spotify.api.url}")
    private String spotifyApiUrl;

    @Autowired
    SpotifyAuthService spotifyAuthService;

    public List<Track> searchTrackByIsrc(String isrc) {
        String url = String.format("%s?q=isrc:\"%s\"&type=track&market=US", spotifyApiUrl, isrc);

        String accessToken = spotifyAuthService.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode nodes = parseTrackData(response.getBody());
            return parseTrackDataToList(nodes);
        } catch (HttpClientErrorException e) {
            logger.error("--searchTrackByIsrc() - ", e);
            throw new RuntimeException("Failed to retrieve track data", e);
        }
    }

    private JsonNode parseTrackData(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            // Extract the tracks from the response
            return jsonNode.path("tracks");
        } catch (Exception e) {
            logger.error("--parseTrackData() - ", e);
            throw new RuntimeException("Failed to parse track data", e);
        }
    }

    public List<Track> parseTrackDataToList(JsonNode jsonNode) {
        List<Track> tracks = new ArrayList<>();
        if (!validNode(jsonNode))
        {
            logger.debug("--parseTrackDataToList() - jsonNode is null or empty");
            return tracks;
        }
        // Assuming that the response JSON from Spotify API contains a "items" node
        JsonNode itemsNode = jsonNode.path("items");
        // Iterate over each item in the response
        for (JsonNode item : itemsNode) {
            Track track = new Track();

            // Set the track's properties from the response
            track.setId(UUID.randomUUID());  // Set a new UUID for the track's ID
            track.setName(item.path("name").asText());
            track.setIsrc(item.path("external_ids").path("isrc").asText());
            JsonNode albumNode = item.path("album");
            if(validNode(albumNode)) {
                JsonNode artists = albumNode.path("artists");
                if(validNode(artists))
                    // Assuming first artist in the array
                    track.setArtistName(artists.get(0).path("name").asText());
                track.setAlbumName(albumNode.path("name").asText());
                track.setAlbumId(albumNode.path("id").asText());
            }
            track.setExplicit(item.path("explicit").asBoolean());
            track.setPlaybackSeconds(item.path("duration_ms").asLong() / 1000);  // Duration is in milliseconds, convert to seconds

            // Add the track to the list
            tracks.add(track);
        }
        return tracks;
    }

    private boolean validNode(JsonNode jsonNode) {
        return Objects.nonNull(jsonNode) || !jsonNode.isEmpty();
    }
}
