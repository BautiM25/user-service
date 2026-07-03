package ar.edu.unnoba.pdyc2026.user_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.unnoba.pdyc2026.user_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmail(String email);
    @Query("SELECT DISTINCT u.username FROM User u LEFT JOIN u.favoriteEventIds fe LEFT JOIN u.followedArtistIds fa WHERE fe = :eventId OR fa IN :artistIds")
    List<String> findInterestedUsernames(@Param("eventId") Long eventId, @Param("artistIds") List<Long> artistIds);
}