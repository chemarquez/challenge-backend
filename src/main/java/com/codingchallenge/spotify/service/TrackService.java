package com.codingchallenge.spotify.service;


import com.codingchallenge.spotify.model.Track;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrackService {

    Track createTrack(String name, String artistName, String albumName, String albumId, boolean isExplicit, long playbackSeconds);

    Track createTrack(String isrc);

    Optional<Track> findTrackById(UUID id);

    Track findTrackByIsrc(String isrc);

    Track findByAlbumId(String albumId);

    List<Track> getAllTracks();

    Track updateTrack(UUID id, String name, String artistName, String albumName, String albumId, boolean isExplicit, long playbackSeconds);

    boolean deleteTrack(UUID id);

    void deleteAllTracks();
}
