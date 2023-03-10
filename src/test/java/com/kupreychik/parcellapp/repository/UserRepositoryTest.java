package com.kupreychik.parcellapp.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.kupreychik.parcellapp.annotation.PersistenceLayerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@PersistenceLayerTest
@DisplayName("Persistent UserRepositoryTest")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DataSet(value = "datasets/user/users.yml", cleanBefore = true, cleanAfter = true)
    void should_find_user_by_email() {
        var user = userRepository.findByEmail("email@email.com");
        assertTrue(user.isPresent());
    }

}