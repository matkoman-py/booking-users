package com.bookingusers.model.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookingKeycloakUserId implements Serializable {

    @Serial
    private static final long serialVersionUID = 7517264957536092946L;

    private String bookingId;

    private String keycloakId;
}
