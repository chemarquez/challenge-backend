package com.codingchallenge.spotify.service.impl;

import com.codingchallenge.spotify.model.Track;
import com.codingchallenge.spotify.repository.TrackRepository;
import com.codingchallenge.spotify.service.SpotifyService;
import com.codingchallenge.spotify.service.TrackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TrackServiceImpl implements TrackService {
    private static final Logger logger = LogManager.getLogger(com.codingchallenge.spotify.service.TrackService.class);

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private SpotifyService spotifyService;

    public Track createTrack(String name, String artistName, String albumName, String albumId, boolean isExplicit, long playbackSeconds) {
        Track track = new Track();
        track.setName(name);
        track.setArtistName(artistName);
        track.setAlbumName(albumName);
        track.setAlbumId(albumId);
        track.setExplicit(isExplicit);
        track.setPlaybackSeconds(playbackSeconds);

        return trackRepository.save(track);  // Persist the track
    }

    @Override
    public Track createTrack(String isrc) {
        Track track = this.findTrackByIsrc(isrc);
        if(Objects.isNull(track)) {
            List<Track> trackList = spotifyService.searchTrackByIsrc(isrc);
            if (trackList.isEmpty()) {
                logger.debug("--createTrack(String isrc) - trackList is empty");
                return null;
            }
            return trackRepository.save(trackList.get(0));
        }
        return track;
    }

    public Optional<Track> findTrackById(UUID id) {
        return trackRepository.findById(id);
    }

    @Override
    public Track findTrackByIsrc(String isrc) {
        return trackRepository.findFirstByIsrc(isrc);
    }

    @Override
    public Track findByAlbumId(String albumId) {
        return trackRepository.findByAlbumId(albumId);
    }

    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    public Track updateTrack(UUID id, String name, String artistName, String albumName, String albumId, boolean isExplicit, long playbackSeconds) {
        Optional<Track> existingTrackOptional = trackRepository.findById(id);
        if (existingTrackOptional.isPresent()) {
            Track track = existingTrackOptional.get();
            track.setName(name);
            track.setArtistName(artistName);
            track.setAlbumName(albumName);
            track.setAlbumId(albumId);
            track.setExplicit(isExplicit);
            track.setPlaybackSeconds(playbackSeconds);

            return trackRepository.save(track);
        }
        return null;  // Track not found
    }

    public boolean deleteTrack(UUID id) {
        Optional<Track> existingTrackOptional = trackRepository.findById(id);
        if (existingTrackOptional.isPresent()) {
            trackRepository.deleteById(id);  // Delete the track by its ID
            return true;  // Return true if deleted successfully
        }
        return false;  // Track not found, nothing to delete
    }

    public void deleteAllTracks() {
        trackRepository.deleteAll();
    }
}
