package ar.edu.unnoba.pdyc2026.user_service.dto;

import ar.edu.unnoba.pdyc2026.user_service.model.Genre;

public record ArtistResponse(Long id, String name, Genre genre, boolean active) {}
