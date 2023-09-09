package com.bookingusers.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "booking_keycloak_user")
public class BookingKeycloakUser {

    @EmbeddedId
    private BookingKeycloakUserId id;
}
