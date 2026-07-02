package ar.edu.unnoba.pdyc2026.user_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.unnoba.pdyc2026.user_service.client.CatalogClient;
import ar.edu.unnoba.pdyc2026.user_service.dto.ArtistResponse;
import ar.edu.unnoba.pdyc2026.user_service.dto.EventDetail;
import ar.edu.unnoba.pdyc2026.user_service.dto.EventSummary;
import ar.edu.unnoba.pdyc2026.user_service.model.EventStatus;
import ar.edu.unnoba.pdyc2026.user_service.model.User;
import ar.edu.unnoba.pdyc2026.user_service.repository.UserRepository;

@Service
public class UserActionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CatalogClient catalogClient;

    private User getOrCreateUser(String username) {
        String normalized = username.toLowerCase();
        return userRepository.findByUsernameIgnoreCase(normalized)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(normalized);
                    newUser.setEmail(normalized + "@example.com"); 
                    return userRepository.save(newUser);
                });
    }

    @Transactional
    public CompletableFuture<Void> followArtist(String username, Long artistId) {
        return CompletableFuture.runAsync(() -> {
            User user = getOrCreateUser(username);
            
            // Validamos contra el catálogo que el artista exista
            ArtistResponse artist = catalogClient.getArtistById(artistId);
            if (!artist.active()) {
                throw new RuntimeException("Cannot follow an inactive artist");
            }
            
            if (!user.getFollowedArtistIds().contains(artistId)) {
                user.getFollowedArtistIds().add(artistId);
                userRepository.save(user);
            }
        });
    }

    @Transactional
    public CompletableFuture<Void> unfollowArtist(String username, Long artistId) {
        return CompletableFuture.runAsync(() -> {
            User user = userRepository.findByUsernameIgnoreCase(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            user.getFollowedArtistIds().remove(artistId);
            userRepository.save(user);
        });
    }

    @Transactional
    public CompletableFuture<Void> favoriteEvent(String username, Long eventId) {
        return CompletableFuture.runAsync(() -> {
            User user = getOrCreateUser(username);
            
            // Verificamos que el evento exista llamando al catálogo
            catalogClient.getEventById(eventId);
            
            if (!user.getFavoriteEventIds().contains(eventId)) {
                user.getFavoriteEventIds().add(eventId);
                userRepository.save(user);
            }
        });
    }

    @Transactional
    public CompletableFuture<Void> unfavoriteEvent(String username, Long eventId) {
        return CompletableFuture.runAsync(() -> {
            User user = userRepository.findByUsernameIgnoreCase(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            user.getFavoriteEventIds().remove(eventId);
            userRepository.save(user);
        });
    }

    @Transactional(readOnly = true)
    public CompletableFuture<List<ArtistResponse>> getFollowingArtists(String username) {
        return CompletableFuture.supplyAsync(() -> {
            User user = getOrCreateUser(username);
            
            List<ArtistResponse> following = new ArrayList<>();
            for (Long artistId : user.getFollowedArtistIds()) {
                try {
                    following.add(catalogClient.getArtistById(artistId));
                } catch (Exception e) {
                    // Ignoramos si el artista fue borrado del catálogo
                }
            }
            return following;
        });
    }

    @Transactional(readOnly = true)
    public CompletableFuture<List<EventSummary>> getFavoriteEvents(String username) {
        return CompletableFuture.supplyAsync(() -> {
            User user = getOrCreateUser(username);
            LocalDate today = LocalDate.now();
            
            List<EventSummary> favoriteEvents = new ArrayList<>();
            for (Long eventId : user.getFavoriteEventIds()) {
                try {
                    EventDetail detail = catalogClient.getEventById(eventId);
                    if (!detail.startDate().isBefore(today) && detail.status() != EventStatus.tentative) {
                        favoriteEvents.add(new EventSummary(
                                detail.id(), detail.name(), detail.startDate(), 
                                detail.status(), detail.artists().size()));
                    }
                } catch (Exception e) {}
            }
            
            return favoriteEvents.stream()
                    .sorted(Comparator.comparing(EventSummary::startDate))
                    .collect(Collectors.toList());
        });
    }

    @Transactional(readOnly = true)
    public CompletableFuture<List<EventSummary>> getFollowingEvents(String username) {
        return CompletableFuture.supplyAsync(() -> {
            User user = getOrCreateUser(username);
            
            List<EventSummary> allEvents = new ArrayList<>();
            for (Long artistId : user.getFollowedArtistIds()) {
                try {
                    allEvents.addAll(catalogClient.getEventsByArtist(artistId));
                } catch (Exception e) {}
            }

            LocalDate today = LocalDate.now();
            return allEvents.stream()
                    .distinct() // Evita duplicados
                    .filter(event -> !event.startDate().isBefore(today))
                    .filter(event -> event.status() != EventStatus.tentative)
                    .sorted(Comparator.comparing(EventSummary::startDate))
                    .collect(Collectors.toList());
        });
    }
}