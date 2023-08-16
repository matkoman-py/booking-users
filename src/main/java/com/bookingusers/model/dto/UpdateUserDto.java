package com.bookingusers.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateUserDto {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String residence;
}
