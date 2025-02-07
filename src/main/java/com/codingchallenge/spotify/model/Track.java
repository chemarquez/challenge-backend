package com.codingchallenge.spotify.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tracks",  schema = "spotifydb", indexes = {
        @Index(name = "idx_album_id", columnList = "album_id"),
        @Index(name = "idx_isrc", columnList = "isrc"),
        @Index(name = "idx_artist_name", columnList = "artist_name"),
        @Index(name = "idx_name", columnList = "name")
})
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(name = "artist_name", length = 200, nullable = false)
    private String artistName;

    @Column(name = "album_name", length = 200, nullable = false)
    private String albumName;

    @Column(name = "album_id", length = 200, nullable = false)
    private String albumId;

    @Column(name = "isrc", length = 12, nullable = false, unique = true)  // ISRC length is typically 12 characters
    private String isrc;

    @Column(name = "is_explicit", nullable = false)
    private boolean isExplicit;

    @Column(name = "playback_seconds", nullable = false)
    private long playbackSeconds;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public boolean isExplicit() {
        return isExplicit;
    }

    public void setExplicit(boolean explicit) {
        isExplicit = explicit;
    }

    public long getPlaybackSeconds() {
        return playbackSeconds;
    }

    public void setPlaybackSeconds(long playbackSeconds) {
        this.playbackSeconds = playbackSeconds;
    }
}
