package ar.edu.unnoba.pdyc2026.user_service.dto;

import java.time.LocalDate;

import ar.edu.unnoba.pdyc2026.user_service.model.EventStatus;

public record EventSummary(Long id, String name, LocalDate startDate, EventStatus status, int artistCount) {}