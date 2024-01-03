package springboottemplate.data_services.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.enums.Role;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail_WhenEmailExists_ReturnsUser() {
        // Arrange
        User user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        userRepository.save(user1);


        // Act
        Optional<User> foundUser1 = userRepository.findByEmail(user1.getEmail());

        // Assert
        assertThat(foundUser1).isPresent();

        assertThat(foundUser1.get().getEmail()).isEqualTo(user1.getEmail());

    }

    @Test
    public void findByEmail_WhenEmailDoesNotExist_ReturnsEmptyOptional() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    public void findByFirstNameOrLastNameContainingIgnoreCase_WhenMatchesExist_ReturnsMatchingUsers() {
        // Arrange
        User user1 = createUser("John Doe");
        User user2 = createUser("Jane Smith");
        userRepository.saveAll(List.of(user1, user2));

        // Act
        String query = "John";
        List<User> foundUsers = userRepository.searchByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);

        // Assert
        assertThat(foundUsers).hasSize(1);
        assertThat(foundUsers.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    public void findByFirstNameOrLastNameContainingIgnoreCase_WhenNoMatchesExist_ReturnsEmptyList() {
        // Act
        List<User> foundUsers = userRepository.searchByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("Nonexistent", "");

        // Assert
        assertThat(foundUsers).isEmpty();
    }

    private User createUser(String fullName) {
        String[] parts = fullName.split("\\s");
        return User.builder()
                .firstName(parts[0])
                .lastName(parts[1])
                .email(parts[0].toLowerCase() + "." + parts[1].toLowerCase() + "@example.com")
                .password("password")
                .role(Role.USER)
                .build();
    }
}