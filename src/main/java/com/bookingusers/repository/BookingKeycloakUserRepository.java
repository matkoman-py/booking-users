package com.bookingusers.repository;

import com.bookingusers.model.entity.BookingKeycloakUser;
import com.bookingusers.model.entity.BookingKeycloakUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingKeycloakUserRepository extends JpaRepository<BookingKeycloakUser, BookingKeycloakUserId> {

    Optional<BookingKeycloakUser> findByIdKeycloakId(String keycloakId);
}
