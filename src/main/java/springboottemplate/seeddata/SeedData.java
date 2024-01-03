package springboottemplate.seeddata;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboottemplate.auth.AuthenticationService;
import springboottemplate.auth.RegisterRequest;
import springboottemplate.data_services.friendship.FriendshipService;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.UserProfile;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.*;

@RequiredArgsConstructor

@Configuration
public class SeedData {
    private final AuthenticationService authenticationService;
    private final ImageService imageService;
    private final PostRepository postRepository;
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final FriendshipService friendshipService;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            if (args.length == 0 || !args[0].equals("seed"))
                return;

            List<Map.Entry<String, String>> users = new ArrayList<>(List.of(
                    Map.entry("Alin", "Luca"),
                    Map.entry("Horea", "Oros"),
                    Map.entry("Eugen", "Laslo"),
                    Map.entry("Remus", "Pelle")
            ));

            for (Map.Entry<String, String> u : users) {
                String firstName = u.getKey();
                String lastName = u.getValue();
                String email = firstName.toLowerCase() + lastName.toLowerCase() + "@gmail.com";
                RegisterRequest registerRequest = RegisterRequest
                        .builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password("abc")
                        .build();
                authenticationService.register(registerRequest);

                User user = userRepository.findByEmail(email).orElseThrow();
                user.setUserProfile(
                        UserProfile
                                .builder()
                                .profileImage(imageService.getRandomImage())
                                .build()
                );

                userRepository.save(user);
            }

        };
    }
}
