package com.bookingusers.model.dto;

import com.bookingusers.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateUserDto {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String residence;
    private UserRole role;
}
