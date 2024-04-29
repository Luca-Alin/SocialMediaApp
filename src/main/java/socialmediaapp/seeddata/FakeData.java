package springboottemplate.seeddata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboottemplate.data_services.comment.model.Comment;
import springboottemplate.data_services.comment.repository.CommentRepository;
import springboottemplate.data_services.friendship.model.Friendship;
import springboottemplate.data_services.friendship.model.FriendshipStatus;
import springboottemplate.data_services.friendship.repository.FriendshipRepository;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.message.repository.MessageRepository;
import springboottemplate.data_services.post.model.Post;
import springboottemplate.data_services.post.model.PostImage;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.user.enums.Role;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.model.UserProfile;
import springboottemplate.data_services.user.repository.UserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


@RequiredArgsConstructor

@Configuration
@Slf4j
public class FakeData {


    private final PostRepository postRepository;
    private final FriendshipRepository friendshipRepository;
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final MessageRepository messageRepository;
    public List<byte[]> images = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/images.txt"))))
            .lines()
            .map(line -> Base64.getDecoder().decode(line))
            .toList();
    public List<String> quotes = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/quotes.txt"))))
            .lines()
            .toList();


    @Bean
    public CommandLineRunner commandLineRunner() {
        return _ -> {
            double time = System.currentTimeMillis();

            generateUsers("John Smith");
            generateUsers("James Doe");
            generateUsers("Robert Doe");
            generateUsers("Maria Rodriguez");

            generateFriendships();

            generatePosts();

            generateComments();

            generateMessages();

            System.out.printf("Database data generated in: %.2fs\n", (System.currentTimeMillis() - time) / 1000d);
        };
    }

    public void generateUsers(String fullName) {
        // this is a hash for the word "password"
        String hashedPassword = "$2a$10$SOAyQMmhTFRbf9drP3Myy.NDNNnaJQkMUhxauq2LucaCSBjwIciAK";


        String firstName = fullName.split(" ")[0];
        String lastName = fullName.split(" ")[1];
        String email = "%s_%s@social-media.app".formatted(firstName.toLowerCase(), lastName.toLowerCase());
        String bio = "Hello, I am %s %s.".formatted(firstName, lastName);

        User user = User
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .userProfile(
                        UserProfile
                                .builder()
                                .profileImage(randomImage())
                                .bio(bio)
                                .build()
                )
                .password(hashedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public void generateFriendships() {
        List<User> users = userRepository.findAll();

        List<Friendship> friendships = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                if (i == j) continue;

                Friendship friendship = Friendship
                        .builder()
                        .sender(users.get(i))
                        .receiver(users.get(j))
                        .friendshipStatus(FriendshipStatus.ACCEPTED)
                        .build();

                friendships.add(friendship);
            }
        }
        friendshipRepository.saveAll(friendships);
    }

    private void generatePosts() {
        List<User> users = userRepository.findAll();

        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        list.stream().parallel().forEach(_ -> {

            List<String> images = new ArrayList<>();
            for (int j = 0; j < random.nextInt(0, 4); j++) images.add("image");
            List<byte[]> postImages = new ArrayList<>();
            images.stream()
                    .parallel()
                    .forEach(_ -> postImages.add(randomImage()));
            String postContent = randomText();

            Post post = Post
                    .builder()
                    .user(users.get(random.nextInt(users.size())))
                    .content(postContent)
                    .images(new ArrayList<>(postImages
                            .stream()
                            .map((img) -> PostImage
                                    .builder()
                                    .image(img)
                                    .build())
                            .toList()))
                    .createdAt(new Date(random.nextLong(System.currentTimeMillis())))
                    .build();
            postRepository.save(post);
        });

    }

    private void generateComments() {
        List<Comment> comments = new ArrayList<>();

        List<Post> posts = postRepository.findAll();
        List<User> users = userRepository.findAll();

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) list.add(i);
        list.stream().parallel().forEach(_ -> {
            User user = users.get(random.nextInt(users.size()));
            Post post = posts.get(random.nextInt(posts.size()));
            String content = randomText();
            Comment comment = Comment
                    .builder()
                    .post(post)
                    .user(user)
                    .createdAt(new Date(random.nextLong(post.getCreatedAt().getTime(), System.currentTimeMillis())))
                    .content(content)
                    .build();
            comments.add(comment);
        });
        comments.sort(Comparator.comparingLong(a -> a.getCreatedAt().getTime()));

        commentRepository.saveAll(comments);
    }

    public void generateMessages() {
        List<Message> messages = new ArrayList<>();

        List<User> users = userRepository.findAll();

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) list.add(i);

        list.stream().parallel().forEach(_ -> {
            User sender = users.get(random.nextInt(users.size()));
            User receiver = users.get(random.nextInt(users.size()));
            if (sender.equals(receiver)) return;


            String content = randomText();

            Message message = Message
                    .builder()
                    .sender(sender)
                    .receiver(receiver)
                    .content(content)
                    .dateSent(new Date(random.nextLong(System.currentTimeMillis())))
                    .build();
            messages.add(message);
        });

        messages.sort(Comparator.comparingLong(a -> a.getDateSent().getTime()));
        messageRepository.saveAll(messages);
    }

    public byte[] randomImage() {
        return images.get(random.nextInt(images.size()));
    }

    public String randomText() {
        return quotes.get(random.nextInt(quotes.size()));
    }

}
