package com.bookingusers.service;

import com.bookingusers.exception.ActiveBookingsException;
import com.bookingusers.exception.NotFoundException;
import com.bookingusers.keycloak.KeycloakAdminClient;
import com.bookingusers.model.dto.CreateUserDto;
import com.bookingusers.model.dto.ReadUserDto;
import com.bookingusers.model.dto.UpdateUserDto;
import com.bookingusers.model.entity.BookingKeycloakUser;
import com.bookingusers.model.entity.User;
import com.bookingusers.model.enums.UserRole;
import com.bookingusers.proxy.BookingAccommodationProxy;
import com.bookingusers.repository.BookingKeycloakUserRepository;
import com.bookingusers.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BookingKeycloakUserRepository bookingKeycloakUserRepository;

    @Mock
    KeycloakAdminClient keycloakClient;

    @Mock
    BookingAccommodationProxy proxy;

    @InjectMocks
    UserService userService;

    @Test
    void createUser_whenRequestIsValid_thenReturnCreatedUser() {
        //Given
        User user = new User();
        user.setId("id");
        String keycloakId = "keycloakId";
        BookingKeycloakUser bookingKeycloakUser = new BookingKeycloakUser();

        CreateUserDto createUserDto = new CreateUserDto();

        Mockito.when(userRepository.save(any())).thenReturn(user);
        Mockito.when(keycloakClient.createUser(any(), any())).thenReturn(keycloakId);
        Mockito.when(bookingKeycloakUserRepository.save(any())).thenReturn(bookingKeycloakUser);
        //When
        userService.create(createUserDto);
        //Then
        verify(userRepository).save(any());
        verify(keycloakClient).createUser(any(), any());
        verify(bookingKeycloakUserRepository).save(any());
    }

    @Test
    void getOne_whenRequestIsValid_thenReturnUser() {
        //Given
        User user = new User();
        user.setId("id");
        user.setUsername("username");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        //When
        ReadUserDto realResponse = userService.getOne("id");
        //Then
        verify(userRepository).findById(any());
        Assertions.assertEquals("id", realResponse.getId());
        Assertions.assertEquals("username", realResponse.getUsername());
        Assertions.assertEquals("firstName", realResponse.getFirstName());
        Assertions.assertEquals("lastName", realResponse.getLastName());
    }

    @Test
    void getOne_whenUserWithProvidedIdDoesntExist_thenThrowNotFoundException() {
        //Given
        Mockito.when(userRepository.findById(any())).thenThrow(NotFoundException.class);
        //When
        assertThrows(NotFoundException.class, () -> userService.getOne("id"));
        //Then
        verify(userRepository).findById(any());
    }

    @Test
    void getAll_whenRequestIsValid_thenReturnListOfUsers() {
        //Given
        User user1 = new User();
        user1.setId("id1");
        User user2 = new User();
        user2.setId("id2");

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        //When
        List<ReadUserDto> realResponse = userService.getAll();
        //Then
        verify(userRepository).findAll();
        Assertions.assertEquals(2, realResponse.size());
    }

    @Test
    void update_whenRequestIsValid_thenReturnUpdatedUser() {
        //Given
        User user = new User();
        user.setId("id");
        UpdateUserDto updateUserDto = new UpdateUserDto();
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);
        doNothing().when(keycloakClient).updateUser(any(), any(), any());
        //When
        ReadUserDto updatedUser = userService.update(updateUserDto, "id", "keycloakId");
        //Then
        verify(userRepository).findById(any());
        verify(userRepository).save(any());
        verify(keycloakClient).updateUser(any(), any(), any());
        assertEquals("id", updatedUser.getId());
    }

    @Test
    void update_whenUserWithProvidedIdDoesntExist_thenThrowNotFoundException() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        //Given
        Mockito.when(userRepository.findById(any())).thenThrow(NotFoundException.class);
        //When
        assertThrows(NotFoundException.class, () -> userService.update(updateUserDto, "id", "keycloakId"));
        //Then
        verify(userRepository).findById(any());
    }

    @Test
    void delete_whenRequestIsValidForGuest_thenDeleteUser() {
        //Given
        User user = new User();
        user.setId("id");
        user.setRole(UserRole.GUEST);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(proxy.doActiveBookingsExistForGuest(any())).thenReturn(false);
        doNothing().when(userRepository).delete(any());
        //When
        userService.delete("id");
        //Then
        verify(userRepository).findById(any());
        verify(proxy).doActiveBookingsExistForGuest(any());
        verify(userRepository).delete(any());
    }

    @Test
    void delete_whenRequestIsValidForHost_thenDeleteUser() {
        //Given
        User user = new User();
        user.setId("id");
        user.setRole(UserRole.HOST);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(proxy.doActiveBookingsExistForHost(any())).thenReturn(false);
        Mockito.when(proxy.deleteAllForHost(any())).thenReturn("SUCCESS");
        doNothing().when(userRepository).delete(any());
        //When
        userService.delete("id");
        //Then
        verify(userRepository).findById(any());
        verify(proxy).doActiveBookingsExistForHost(any());
        verify(proxy).deleteAllForHost(any());
        verify(userRepository).delete(any());
    }

    @Test
    void delete_whenUserWithProvidedIdDoesntExist_thenThrowNotFoundException() {
        //Given
        Mockito.when(userRepository.findById(any())).thenThrow(NotFoundException.class);
        //When
        assertThrows(NotFoundException.class, () -> userService.delete("id"));
        //Then
        verify(userRepository).findById(any());
    }

    @Test
    void delete_whenGuestHaveActiveBookings_thenThrowActiveBookingsException() {
        //Given
        User user = new User();
        user.setId("id");
        user.setRole(UserRole.GUEST);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(proxy.doActiveBookingsExistForGuest(any())).thenReturn(true);
        //When
        assertThrows(ActiveBookingsException.class, () -> userService.delete("id"));
        //Then
        verify(userRepository).findById(any());
        verify(proxy).doActiveBookingsExistForGuest(any());
    }

    @Test
    void delete_whenHostHaveActiveBookings_thenThrowActiveBookingsException() {
        //Given
        User user = new User();
        user.setId("id");
        user.setRole(UserRole.HOST);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(proxy.doActiveBookingsExistForHost(any())).thenReturn(true);
        //When
        assertThrows(ActiveBookingsException.class, () -> userService.delete("id"));
        //Then
        verify(userRepository).findById(any());
        verify(proxy).doActiveBookingsExistForHost(any());
    }
}
