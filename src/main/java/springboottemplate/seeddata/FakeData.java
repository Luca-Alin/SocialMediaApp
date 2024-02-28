package springboottemplate.seeddata;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import springboottemplate.data_services.comment.model.Comment;
import springboottemplate.data_services.comment.repository.CommentRepository;
import springboottemplate.data_services.friendship.model.Friendship;
import springboottemplate.data_services.friendship.model.FriendshipStatus;
import springboottemplate.data_services.friendship.repository.FriendshipRepository;
import springboottemplate.data_services.post.model.Post;
import springboottemplate.data_services.post.model.PostImage;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.user.enums.Role;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.model.UserProfile;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor

@Configuration
public class FakeData {

    private final PostRepository postRepository;
    private final FriendshipRepository friendshipRepository;
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private final List<String> imagesStrings = new ArrayList<>();
    private final List<String> textStrings = new ArrayList<>();
    private final List<String> fakeNames = new ArrayList<>();

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            long timer = System.currentTimeMillis();

            readFakeData();
            System.out.println("Time to read fake data: " + (System.currentTimeMillis() - timer) / 1000.0);


            List<User> users = generateUsers();

            Thread t1 = new Thread(() -> generateFriendships(users));
            t1.start();

            List<Post> posts = generatePosts(users);
            generateComments(posts, users);
            System.out.println("Time to insert fake data into database: " + (System.currentTimeMillis() - timer) / 1000.0);
        };
    }

    @SuppressWarnings("DataFlowIssue")
    public void readFakeData() throws Exception {
        imagesStrings.addAll(List.of(Images.images.split("\n")));
        fakeNames.addAll(List.of(Names.names.split("\n")));
        textStrings.addAll(List.of(Text.text.split("\n")));
    }

    public List<User> generateUsers() {
        String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());


        List<User> users = new ArrayList<>();
        for (String line : fakeNames) {
            String[] tokens = line.split(" ");
            String firstName = tokens[0];
            String lastName = tokens[1];
            String email = "%s_%s@social-media.app".formatted(firstName.toLowerCase(), lastName.toLowerCase());

            UserProfile userProfile = UserProfile
                    .builder()
                    .profileImage(randomImage())
                    .bio("Hello, I am %s".formatted(firstName))
                    .build();

            User user = User
                    .builder()
                    .email(email)
                    .password(hashedPassword)
                    .firstName(firstName)
                    .lastName(lastName)
                    .userProfile(userProfile)
                    .role(Role.USER)
                    .build();

            users.add(user);
        }


        return userRepository.saveAll(users);
    }

    public void generateFriendships(List<User> users) {

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

    private List<Post> generatePosts(List<User> users) {

        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            List<PostImage> fakeImages = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j < 2 + random.nextInt(5); j++) {
                fakeImages.add(new PostImage(randomImage()));
                sb.append(randomText());
            }


            Post post = Post
                    .builder()
                    .user(users.get(random.nextInt(users.size())))
                    .images(fakeImages)
                    .content(sb.toString())
                    .build();
            posts.add(post);
        }

        return postRepository.saveAll(posts);
    }

    private void generateComments(List<Post> posts, List<User> users) {

        List<Comment> comments = new ArrayList<>();

        for (int i = 0; i < 400; i++) {
            Post post = posts.get(random.nextInt(posts.size()));
            User user = users.get(random.nextInt(users.size()));

            StringBuilder content = new StringBuilder();

            for (int j = 1; j < 2 + random.nextInt(4); j++) {
                content.append(randomText());
            }



            Comment comment = Comment
                    .builder()
                    .user(user)
                    .post(post)
                    .content(content.toString())
                    .build();
            comments.add(comment);
        }

        commentRepository.saveAll(comments);
    }


    public byte[] randomImage() {
        return Base64.getDecoder().decode(
                imagesStrings.get(random.nextInt(imagesStrings.size()))
        );
    }


    public String randomText() {
        return textStrings.get(random.nextInt(textStrings.size())) + " ";
    }
}
