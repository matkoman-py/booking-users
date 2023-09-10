package com.bookingusers.repository;

import com.bookingusers.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles("test-containers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryITest {

    private final String EXISTING_USER_ID = "1";
    private final String NON_EXISTING_USER_ID = "5";

    @Autowired
    UserRepository userRepository;

    @Test
    void getUser_whenUserExistInDatabase_thenReturnUserWithProvidedId() {
        Optional<User> user = userRepository.findById(EXISTING_USER_ID);

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get().getId(), EXISTING_USER_ID);
    }

    @Test
    void getUser_whenUserDoesntExistInDatabase_thenReturnEmptyOptionalObject() {
        Optional<User> user = userRepository.findById(NON_EXISTING_USER_ID);

        Assertions.assertTrue(user.isEmpty());
    }
}
