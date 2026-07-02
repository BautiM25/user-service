package ar.edu.unnoba.pdyc2026.user_service.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection
    @CollectionTable(name = "user_followed_artists", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "artist_id")
    private List<Long> followedArtistIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_favorite_events", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "event_id")
    private List<Long> favoriteEventIds = new ArrayList<>();
    }