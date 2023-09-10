package com.bookingusers.model.entity;


import com.bookingusers.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String residence;
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
