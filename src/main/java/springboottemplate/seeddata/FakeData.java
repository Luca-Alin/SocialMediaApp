package springboottemplate.seeddata;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;


@RequiredArgsConstructor

@Configuration
@Slf4j
public class FakeData {


    private int textIndex = 1;
    private int imageIndex = 1;
    private final PostRepository postRepository;
    private final FriendshipRepository friendshipRepository;
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final MessageRepository messageRepository;
    private final ObjectMapper mapper = new ObjectMapper();

//    @Bean
    public CommandLineRunner commandLineRunner() {
        return _ -> {
            long timer = System.currentTimeMillis();
            generateUsers();
            System.out.printf("Time to generate users: %s%n", (System.currentTimeMillis() - timer) / 1000.0);

            timer = System.currentTimeMillis();
            generateFriendships();
            System.out.printf("Time to generate friendships: %s%n", (System.currentTimeMillis() - timer) / 1000.0);

            timer = System.currentTimeMillis();
            generatePosts();
            System.out.printf("Time to generate posts: %s%n", (System.currentTimeMillis() - timer) / 1000.0);

            timer = System.currentTimeMillis();
            generateComments();
            System.out.printf("Time to generate comments: %s%n", (System.currentTimeMillis() - timer) / 1000.0);

            timer = System.currentTimeMillis();
            generateMessages();
            System.out.printf("Time to generate messages: %s%n", (System.currentTimeMillis() - timer) / 1000.0);

            System.out.println("Done!");
        };
    }


    public void generateUsers() {
        String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

        List<String> usersList = new ArrayList<>(List.of(
                "John Smith",
                "James Doe",
                "Robert Doe",
                "Maria Rodriguez"
        ));


        List<User> users = new ArrayList<>();
        usersList.stream()
                .parallel()
                .forEach(fullName -> {

                    String firstName = fullName.split(" ")[0];
                    String lastName = fullName.split(" ")[1];
                    String email = "%s_%s@social-media.app".formatted(firstName.toLowerCase(), lastName.toLowerCase());
                    byte[] profileImage = randomImage();
                    String bio = "Hello, I am %s %s.".formatted(firstName, lastName);

                    User user = User
                            .builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .userProfile(
                                    UserProfile
                                            .builder()
                                            .profileImage(profileImage)
                                            .bio(bio)
                                            .build()
                            )
                            .password(hashedPassword)
                            .role(Role.USER)
                            .build();

                    users.add(user);
                });

        userRepository.saveAll(users);
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
        List<Post> posts = new ArrayList<>();

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

        list.stream().parallel().forEach((integer) -> {
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
        byte[] imageBytes;

        try {
            int width = random.nextInt(300, 600);
            int height = random.nextInt(300, 600);
            String url = "https://picsum.photos/id/%d/%d/%d".formatted(imageIndex++, width, height);

            URL requestUrl = new URI(url).toURL();

            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            imageBytes = outputStream.toByteArray();
            connection.disconnect();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return imageBytes;
    }

    public String randomText() {
        String text;
        try {
            String url = "https://dummyjson.com/quotes/%d".formatted(textIndex++); // Replace this URL with your JSON API URL
            URL requestUrl = new URI(url).toURL();
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();


            FakeQuote quote = mapper.readValue(response.toString(), FakeQuote.class);
            text = quote.getQuote();

            connection.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return text;
    }

}
