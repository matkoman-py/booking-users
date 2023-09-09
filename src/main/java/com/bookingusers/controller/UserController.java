package com.bookingusers.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bookingusers.model.dto.CreateUserDto;
import com.bookingusers.model.dto.LoginCredentialsDto;
import com.bookingusers.model.dto.ReadUserDto;
import com.bookingusers.model.dto.UpdateUserDto;
import com.bookingusers.model.entity.User;
import com.bookingusers.service.UserService;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.ErrorRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public String login(@RequestBody LoginCredentialsDto loginCredentialsDto) {
        return userService.login(loginCredentialsDto.getUsername(), loginCredentialsDto.getPassword());
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public User create(@RequestBody CreateUserDto userDto) {
        return userService.create(userDto);
    }

    @GetMapping
    public List<ReadUserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ReadUserDto getOne(@PathVariable String id) {
        return userService.getOne(id);
    }

    @PutMapping("/{id}")
    public ReadUserDto update(@RequestHeader(name = "Authorization") String token,
                              @PathVariable String id, @RequestBody UpdateUserDto userDto) {

        DecodedJWT jwtToken = JWT.decode(token.split(" ")[1]);
        String keycloakId = jwtToken.getClaim("sub").toString().replace("\"", "");

        return userService.update(userDto, id, keycloakId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        userService.delete(id);
        return String.format("User with id %s successfully deleted", id);
    }

    @GetMapping("/user-info")
    public ReadUserDto getUserInfo(@RequestHeader(name = "Authorization") String token) {
        DecodedJWT jwtToken = JWT.decode(token.split(" ")[1]);
        String keycloakId = jwtToken.getClaim("sub").toString().replace("\"", "");

        return userService.getUserInfo(keycloakId);
    }

    // Handle NotAuthorizedException and return a custom 401 response
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorRepresentation> handleNotAuthorizedException(NotAuthorizedException ex) {
        ErrorRepresentation error = new ErrorRepresentation();
        error.setErrorMessage("Invalid credentials");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
