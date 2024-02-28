package springboottemplate.data_services.post.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import springboottemplate.data_services.exception.EntityDoesNotExistException;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.EntityShouldNotContainAnId;
import springboottemplate.data_services.exception.UserDoesNotOwnEntityException;
import springboottemplate.data_services.post.exception.InvalidPostException;
import springboottemplate.data_services.post.model.Post;
import springboottemplate.data_services.post.model.PostDTO;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.user.exceptions.UserNotFoundException;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostDTOMapper postDTOMapper;
    @InjectMocks
    private PostService postService;
    private AutoCloseable autocloseable;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void PostService_findAllPostsByUserAndHisFriends_ReturnsListOfPosts() throws UserNotFoundException {
        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);

        Pageable mockPageable = mock(Pageable.class);

        Post mockPost = mock(Post.class);
        PostDTO mockPostDTO = mock(PostDTO.class);
        List<Post> mockPosts = List.of(mockPost);

        when(userRepository.findByEmail(mockUserDetails.getUsername()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.findAllPostsByUserAndHisFriends(mockUser, mockPageable))
                .thenReturn(mockPosts);
        when(postDTOMapper.apply(mockPost))
                .thenReturn(mockPostDTO);

        // Act
        List<PostDTO> result = postService.findAllPostsByUserAndHisFriends(mockUserDetails, mockPageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).size().isEqualTo(1);
        assertThat(result).contains(mockPostDTO);
    }

    @Test
    public void PostService_savePost_ReturnsPost() throws UserNotFoundException, InvalidPostException, EntityShouldNotContainAnId {
        //Arrange
        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);

        Post mockPost = mock(Post.class);
        PostDTO mockPostDTO = mock(PostDTO.class);

        when(userRepository.findByEmail(mockUserDetails.getUsername()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.save(mockPost))
                .thenReturn(mockPost);
        when(mockPost.getContent())
                .thenReturn("abc");
        when(postDTOMapper.apply(mockPost))
                .thenReturn(mockPostDTO);

        //Act
        var post = postService.save(mockUserDetails, mockPost);

        //Assert
        assertThat(post).isNotNull();
    }

    @Test
    public void PostService_updatePost_ReturnsUpdatedPost() throws EntityShouldContainAnId, UserNotFoundException, UserDoesNotOwnEntityException, EntityDoesNotExistException {
        //Arrange
        UserDetails mockUserDetails = mock(UserDetails.class);
        User mockUser = mock(User.class);

        Post mockPost = mock(Post.class);
        PostDTO mockPostDTO = mock(PostDTO.class);

        String userMockUUid = UUID.randomUUID().toString();
        String postMockUuid =  UUID.randomUUID().toString();

        when(userRepository.findByEmail(mockUserDetails.getUsername()))
                .thenReturn(Optional.of(mockUser));
        when(postRepository.save(mockPost))
                .thenReturn(mockPost);
        when(postDTOMapper.apply(mockPost))
                .thenReturn(mockPostDTO);
        when(mockPost.getUser())
                .thenReturn(mockUser);
        when(mockUser.getUuid())
                .thenReturn(userMockUUid);
        when(mockPost.getUuid())
                .thenReturn(postMockUuid);
        when(postRepository.findById(postMockUuid))
                .thenReturn(Optional.of(mockPost));

        //Act
        var post = postService.updatePost(mockUserDetails, mockPost);

        //Assert
        assertThat(post).isNotNull();
    }

    @Test
    public void PostService_deletePost_ReturnsNull() {

    }

    @Test
    public void PostService_findAllPostsByUserId_ReturnsNull() {

    }
}