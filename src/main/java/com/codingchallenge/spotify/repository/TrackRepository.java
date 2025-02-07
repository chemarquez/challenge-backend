package com.codingchallenge.spotify.repository;


import com.codingchallenge.spotify.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface TrackRepository extends JpaRepository<Track, UUID> {

    Track findFirstByIsrc(String isrc);


    // Query to find tracks by Album ID using native SQL
    @Query(value = "SELECT t.id as id, t.name as name, t.artist_name as artistName, t.album_name as albumName, t.album_id as albumId, " +
            "t.isrc as isrc, t.is_explicit as isExplicit, t.playback_seconds as playbackSeconds " +
            "FROM tracks t WHERE t.album_id = :albumId LIMIT 1", nativeQuery = true)
    Track findByAlbumId(String albumId);

}
