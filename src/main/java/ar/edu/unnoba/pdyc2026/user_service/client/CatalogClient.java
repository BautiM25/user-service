package ar.edu.unnoba.pdyc2026.user_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ar.edu.unnoba.pdyc2026.user_service.dto.ArtistResponse;
import ar.edu.unnoba.pdyc2026.user_service.dto.EventDetail;
import ar.edu.unnoba.pdyc2026.user_service.dto.EventSummary;

// Se conecta a la instancia de catalog-event-service registrada en Eureka
@FeignClient(name = "catalog-event-service")
public interface CatalogClient {

    // Endpoint público para obtener los detalles de un evento
    @GetMapping("/events/{eventId}")
    EventDetail getEventById(@PathVariable("eventId") Long eventId);

    // Endpoint público para obtener los eventos de un artista
    @GetMapping("/artists/{artistId}/events")
    List<EventSummary> getEventsByArtist(@PathVariable("artistId") Long artistId);
    
    // Endpoint de admin para verificar que el artista exista
    @GetMapping("/artists/{id}")
    ArtistResponse getArtistById(@PathVariable("id") Long id);
}