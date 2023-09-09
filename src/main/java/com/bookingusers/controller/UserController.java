package com.bookingusers.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bookingusers.model.dto.CreateUserDto;
import com.bookingusers.model.dto.ReadUserDto;
import com.bookingusers.model.dto.UpdateUserDto;
import com.bookingusers.model.entity.User;
import com.bookingusers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
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
    public ReadUserDto update(@PathVariable String id, @RequestBody UpdateUserDto userDto) {
        return userService.update(userDto, id);
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
}
