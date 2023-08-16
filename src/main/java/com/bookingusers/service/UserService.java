package com.bookingusers.service;


import com.bookingusers.exception.ActiveBookingsException;
import com.bookingusers.exception.NotFoundException;
import com.bookingusers.mapper.UserMapper;
import com.bookingusers.model.enums.UserRole;
import com.bookingusers.model.dto.CreateUserDto;
import com.bookingusers.model.dto.ReadUserDto;
import com.bookingusers.model.dto.UpdateUserDto;
import com.bookingusers.model.entity.User;
import com.bookingusers.proxy.BookingAccommodationProxy;
import com.bookingusers.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BookingAccommodationProxy proxy;

    public User create(CreateUserDto userDto) {
        return userRepository.save(UserMapper.INSTANCE.toEntity(userDto));
    }

    public ReadUserDto getOne(String id) {
        return UserMapper.INSTANCE.toDto(userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Entity with id %s does not exist", id))));
    }

    public List<ReadUserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public ReadUserDto update(UpdateUserDto userDto, String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Entity with id %s does not exist", id)));
        UserMapper.INSTANCE.updateEntityFromDto(userDto, user);
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    public void delete(String id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Entity with id %s does not exist", id)));
        boolean activeBookingsExist = user.getRole().equals(UserRole.GUEST) ?
                proxy.doActiveBookingsExistForGuest(user.getId()) :
                proxy.doActiveBookingsExistForHost(user.getId());
        if(activeBookingsExist) throw new ActiveBookingsException("User has active bookings in the future");
        if(user.getRole().equals(UserRole.HOST)) proxy.deleteAllForHost(user.getId());
        userRepository.delete(user);
    }
}
