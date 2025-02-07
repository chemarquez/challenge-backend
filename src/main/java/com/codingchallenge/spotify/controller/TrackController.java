package com.codingchallenge.spotify.controller;

import com.codingchallenge.spotify.model.Track;
import com.codingchallenge.spotify.service.TrackService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tracks")  // Base path for all endpoints in the controller
public class TrackController {

    @Autowired
    TrackService trackService;

    /**
     * Create a new track.
     *
     * This endpoint creates a new track by receiving track details in the request body.
     *
     * @param track The track to be created.
     * @return The created track with status 201 (Created).
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Track successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Track.class))),
            @ApiResponse(responseCode = "400", description = "Invalid track data")
    })
    @RequestMapping(method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Track> createTrack(@RequestBody Track track) {
        Track createdTrack = trackService.createTrack(
                track.getName(),
                track.getArtistName(),
                track.getAlbumName(),
                track.getAlbumId(),
                track.isExplicit(),
                track.getPlaybackSeconds()
        );
        return new ResponseEntity<>(createdTrack, HttpStatus.CREATED);
    }

    /**
     * Create a new track.
     *
     * This endpoint creates a new track by receiving track details in the request body.
     *
     * @param isrc The track to be created.
     * @return The created track with status 201 (Created).
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Track successfully created",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid track data")
    })
    @RequestMapping(value = "/createTrack", method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Track> createTrack(@RequestParam String isrc) {
        Track createdTrack = trackService.createTrack(isrc);
        return new ResponseEntity<>(createdTrack, HttpStatus.CREATED);
    }

    /**
     * Retrieve a track by its ID.
     *
     * This endpoint retrieves the track for the given ID.
     * If the track is found, a 200 OK response is returned.
     * If not found, a 404 Not Found response is returned.
     *
     * @param id The ID of the track to retrieve.
     * @return The track with status 200 (OK) if found, or status 404 (Not Found) if not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Track found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Track.class))),
            @ApiResponse(responseCode = "404", description = "Track not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Track> getTrackById(@PathVariable UUID id) {
        Optional<Track> track = trackService.findTrackById(id);
        return track.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Retrieve a track by its isrc.
     *
     * This endpoint retrieves the track for the given isrc.
     * If the track is found, a 200 OK response is returned.
     * If not found, a 404 Not Found response is returned.
     *
     * @param isrc The isrc of the track to retrieve.
     * @return The track with status 200 (OK) if found, or status 404 (Not Found) if not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Track found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Track.class))),
            @ApiResponse(responseCode = "404", description = "Track not found")
    })
    @RequestMapping(value = "/getTrackByISRC", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Track> getTrackByISRC(@RequestParam String isrc) {
        Track track = trackService.findTrackByIsrc(isrc);
        return track != null
                ? new ResponseEntity<>(track, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Retrieve all tracks.
     *
     * This endpoint retrieves a list of all tracks.
     * If no tracks exist, an empty list is returned with status 200 OK.
     *
     * @return List of all tracks with status 200 (OK).
     */
    @ApiResponse(responseCode = "200", description = "All tracks retrieved",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Track.class))))
    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<List<Track>> getAllTracks() {
        List<Track> tracks = trackService.getAllTracks();
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    /**
     * Update an existing track by its ID.
     *
     * This endpoint updates an existing track's details by its ID.
     * If the track is found and successfully updated, a 200 OK response is returned.
     * If no track exists with the provided ID, a 404 Not Found response is returned.
     *
     * @param id The ID of the track to update.
     * @param track The updated track data.
     * @return The updated track with status 200 (OK) or status 404 (Not Found) if not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Track successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Track.class))),
            @ApiResponse(responseCode = "404", description = "Track not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Track> updateTrack(@PathVariable UUID id, @RequestBody Track track) {
        Track updatedTrack = trackService.updateTrack(
                id,
                track.getName(),
                track.getArtistName(),
                track.getAlbumName(),
                track.getAlbumId(),
                track.isExplicit(),
                track.getPlaybackSeconds()
        );
        return updatedTrack != null
                ? new ResponseEntity<>(updatedTrack, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Delete a track by its ID.
     *
     * This endpoint deletes the track by the given ID.
     * A 204 No Content response is returned on successful deletion.
     * A 404 Not Found response is returned if the track is not found.
     *
     * @param id The ID of the track to delete.
     * @return Status 204 (No Content) if the track is deleted, or status 404 (Not Found) if not found.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Track successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Track not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
            produces = "application/json")
    public ResponseEntity<Void> deleteTrack(@PathVariable UUID id) {
        boolean isDeleted = trackService.deleteTrack(id);
        return isDeleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Delete all tracks.
     *
     * This endpoint deletes all tracks from the database.
     * A 204 No Content response is returned upon successful deletion.
     *
     * @return Status 204 (No Content) if all tracks are deleted.
     */
    @ApiResponse(responseCode = "204", description = "All tracks successfully deleted")
    @RequestMapping(method = RequestMethod.DELETE,
            produces = "application/json")
    public ResponseEntity<Void> deleteAllTracks() {
        trackService.deleteAllTracks();
        return ResponseEntity.noContent().build();
    }
}
