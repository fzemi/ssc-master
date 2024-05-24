package com.fzemi.sscmaster.user;

import com.fzemi.sscmaster.user.entity.User;
import com.fzemi.sscmaster.user.repository.UserRepository;
import com.fzemi.sscmaster.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryIntegrationTests {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalled() {
        // Given:
        User user = TestDataUtils.createTestUserA();

        // When:
        userRepository.insert(user);
        Optional<User> result = userRepository.findById(user.getID());

        // Then:
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    public void testThatUsersCanBeFoundByIsDeletedFalse() {
        User userA = TestDataUtils.createTestUserA();
        User userB = TestDataUtils.createTestUserB(); // isDeleted = true

        userRepository.insert(List.of(userA, userB));
        List<User> result = userRepository.findAllByIsDeletedFalse();

        assertThat(result)
                .isNotEmpty()
                .contains(userA)
                .doesNotContain(userB);
    }

    @Test
    public void testThatUsersCanBeFoundByIsDeletedTrue() {
        User userA = TestDataUtils.createTestUserA();
        User userB = TestDataUtils.createTestUserB(); // isDeleted = true

        userRepository.insert(List.of(userA, userB));
        List<User> result = userRepository.findAllByIsDeletedTrue();

        assertThat(result)
                .isNotEmpty()
                .contains(userB)
                .doesNotContain(userA);
    }
}
