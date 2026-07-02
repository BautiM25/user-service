package ar.edu.unnoba.pdyc2026.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.pdyc2026.user_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmail(String email);
}