package ar.edu.unnoba.pdyc2026.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unnoba.pdyc2026.user_service.repository.UserRepository;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    @Autowired
    private UserRepository userRepository;

    // Endpoint público (solo usado internamente) para buscar usuarios afectados
    @GetMapping("/interested")
    public List<String> getInterestedUsers(@RequestParam Long eventId, @RequestParam List<Long> artistIds) {
        return userRepository.findInterestedUsernames(eventId, artistIds);
    }
}