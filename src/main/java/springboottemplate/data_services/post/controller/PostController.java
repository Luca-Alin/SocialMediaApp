package springboottemplate.data_services.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.exception.EntityDoesNotExistException;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.post.Post;
import springboottemplate.data_services.post.PostDTO;
import springboottemplate.data_services.post.repository.PostRepository;
import springboottemplate.data_services.post.service.PostDTOMapper;
import springboottemplate.data_services.post.service.PostService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final PostDTOMapper postDTOMapper;

    @GetMapping("/all")
    public List<PostDTO> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
         ) throws InterruptedException {
        Thread.sleep(1000);


        return postService.findAll(pageNumber, pageSize);
    }

    @GetMapping("/posts-posted-by-user-details/{userId}")
    public ResponseEntity<Object> getPostsCreatedByUserId(@PathVariable Integer userId
    ) {
        try {
            List<PostDTO> posts = postService.findAllPostsByUserId(userId);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all-posts-by-friends")
    public ResponseEntity<Object> getAllPosts(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        try {
            List<PostDTO> postDTOS = postService.findAllPostsByUsersFriend(userDetails);
            return new ResponseEntity<>(postDTOS, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("Exception", "User not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts-posted-by-user-details")
    public ResponseEntity<Object> getPostsCreatedByUserDetails(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        try {
            List<PostDTO> posts = postService.findAllPostsByUser(userDetails);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post
    ) {
        if (userDetails == null)
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);

        try {
            PostDTO postDTO = postService.save(userDetails, post);
            return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updatePost(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post
    ) {
        if (userDetails == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        throw new RuntimeException();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deletePost(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer id
    ) {
        if (userDetails == null)
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

        try {
            postService.deletePost(userDetails, id);
            return new ResponseEntity<>("Post deleted", HttpStatus.NO_CONTENT);
        } catch (EntityDoesNotExistException e) {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exits", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}