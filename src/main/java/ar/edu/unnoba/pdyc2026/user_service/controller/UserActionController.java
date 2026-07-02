package ar.edu.unnoba.pdyc2026.user_service.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.pdyc2026.user_service.dto.ArtistResponse;
import ar.edu.unnoba.pdyc2026.user_service.dto.EventSummary;
import ar.edu.unnoba.pdyc2026.user_service.dto.FavoriteEventRequest;
import ar.edu.unnoba.pdyc2026.user_service.dto.FollowArtistRequest;
import ar.edu.unnoba.pdyc2026.user_service.service.UserActionService;

@RestController
@RequestMapping("/me")
public class UserActionController {

    @Autowired
    private UserActionService userActionService;

    private String getUsernameFromToken(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return (String) jwtToken.getTokenAttributes().get("preferred_username");
        }
        return authentication.getName();
    }

    @PostMapping("/following")
    public ResponseEntity<Void> followArtist(@RequestBody FollowArtistRequest request, Authentication authentication) 
            throws ExecutionException, InterruptedException {
        String username = getUsernameFromToken(authentication);
        userActionService.followArtist(username, request.artistId()).get();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/following/{artistId}")
    public ResponseEntity<Void> unfollowArtist(@PathVariable Long artistId, Authentication authentication) 
            throws ExecutionException, InterruptedException {
        String username = getUsernameFromToken(authentication);
        userActionService.unfollowArtist(username, artistId).get();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/favorite-events")
    public ResponseEntity<Void> favoriteEvent(@RequestBody FavoriteEventRequest request, Authentication authentication) 
            throws ExecutionException, InterruptedException {
        String username = getUsernameFromToken(authentication);
        userActionService.favoriteEvent(username, request.eventId()).get();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorite-events/{eventId}")
    public ResponseEntity<Void> unfavoriteEvent(@PathVariable Long eventId, Authentication authentication) 
            throws ExecutionException, InterruptedException {
        String username = getUsernameFromToken(authentication);
        userActionService.unfavoriteEvent(username, eventId).get();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/following")
    public ResponseEntity<List<ArtistResponse>> getFollowingArtists(Authentication authentication) 
            throws ExecutionException, InterruptedException {
        String username = getUsernameFromToken(authentication);
        List<ArtistResponse> artists = userActionService.getFollowingArtists(username).get();
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/favorite-events")
    public ResponseEntity<List<EventSummary>> getFavoriteEvents(Authentication authentication) 
            throws ExecutionException, InterruptedException {
        String username = getUsernameFromToken(authentication);
        List<EventSummary> events = userActionService.getFavoriteEvents(username).get();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/following/events")
    public ResponseEntity<List<EventSummary>> getFollowingEvents(Authentication authentication) 
            throws ExecutionException, InterruptedException {
        String username = getUsernameFromToken(authentication);
        List<EventSummary> events = userActionService.getFollowingEvents(username).get();
        return ResponseEntity.ok(events);
    }
}