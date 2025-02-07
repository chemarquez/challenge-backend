package com.codingchallenge.spotify.service;

import com.codingchallenge.spotify.model.Track;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface SpotifyService {

    List<Track> searchTrackByIsrc(String isrc);

    List<Track> parseTrackDataToList(JsonNode jsonNode);
}

