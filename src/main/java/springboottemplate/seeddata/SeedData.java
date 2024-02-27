package springboottemplate.seeddata;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboottemplate.auth.AuthenticationService;
import springboottemplate.auth.models.RegisterRequest;
import springboottemplate.data_services.comment.Comment;
import springboottemplate.data_services.comment.repository.CommentRepository;
import springboottemplate.data_services.post.Post;
import springboottemplate.data_services.post.PostImage;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.UserProfile;
import springboottemplate.data_services.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor

@Configuration
public class SeedData {
    private final AuthenticationService authenticationService;
    private final ImageService imageService;
    private final PostRepository postRepository;
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
//            if (args.length == 0 || !args[0].equals("seed"))
//                return;

            List<Map.Entry<String, String>> users = new ArrayList<>(List.of(
                    Map.entry("Teddy", "Smith"),
                    Map.entry("John", "Doe"),
                    Map.entry("Jane", "Doe"),
                    Map.entry("Bob", "Smith")
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

            List<User> usersList = userRepository.findAll();
            List<Post> posts = new ArrayList<>();
            Supplier<List<PostImage>> generateImages = () -> {
                List<PostImage> images = new ArrayList<>();
                for (int i = 0; i < random.nextInt(1, 6); i++) {
                    images.add(
                            PostImage
                                    .builder()
                                    .image(imageService.getRandomImage())
                                    .build()
                    );
                }
                return images;
            };

            String loremString = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aperiam dolor laboriosam quibusdam quo sed, sunt ullam! Aspernatur ea nemo quisquam!\n" +
                    "Accusamus aliquam, aliquid architecto consectetur cumque error, facere illo, magni minus mollitia nulla odio quaequam quidem repellat similique ullam.\n" +
                    "Accusantium atque delectus, ducimus eos harum itaque nulla provident, quibusdam rem sapiente suscipit temporatenetur ullam, velit voluptas. Delectus, soluta.\n" +
                    "Alias consectetur dolorem ipsum iure, ratione recusandae. Amet nihil numquam quasi quo. Aliquam delectus evenietnemo quas sit tempore, totam?\n" +
                    "Assumenda commodi dolore eligendi eum fugiat illo quaerat sapiente sit unde vero. Ea earum modi, perspiciatispossimus provident totam unde.";
            String[] lorem = loremString.split("\n");

            for (int i = 0; i < 40; i++) {
                Post post = Post
                        .builder()
                        .content(lorem[random.nextInt(lorem.length)])
                        .createdAt(LocalDateTime.now().minusHours(random.nextInt(24 * 7 * 30)))
                        .images(generateImages.get())
                        .user(usersList.get(random.nextInt(usersList.size())))
                        .build();
                posts.add(post);
            }
            posts = postRepository.saveAll(posts);


            List<Comment> comments = new ArrayList<>();
            for (int i = 0; i < 160; i++) {
                Comment comment = Comment
                        .builder()
                        .user(usersList.get(random.nextInt(usersList.size())))
                        .post(posts.get(random.nextInt(posts.size())))
                        .content(lorem[random.nextInt(lorem.length)])
                        .build();

                comments.add(comment);
            }
            commentRepository.saveAll(comments);
        };
    }
}
