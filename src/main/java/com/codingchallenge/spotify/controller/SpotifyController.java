package com.codingchallenge.spotify.controller;


import com.codingchallenge.spotify.model.Track;
import com.codingchallenge.spotify.service.SpotifyAuthService;
import com.codingchallenge.spotify.service.SpotifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

    private final SpotifyAuthService spotifyAuthService;
    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyAuthService spotifyAuthService, SpotifyService spotifyService) {
        this.spotifyAuthService = spotifyAuthService;
        this.spotifyService = spotifyService;
    }

    @RequestMapping(value = "/getAccessToken", method = RequestMethod.GET,
            produces = "application/json")
    public String getAccessToken() {
        return spotifyAuthService.getAccessToken();
    }

    @RequestMapping(value = "/search/track", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<List<Track>> searchTrack(@RequestParam String isrc) {
        return ResponseEntity.ok().body(spotifyService.searchTrackByIsrc(isrc));
    }
}
