package springboottemplate.data_services.post.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import springboottemplate.data_services.exception.InvalidEmailException;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.post.Post;
import springboottemplate.data_services.post.PostDTO;
import springboottemplate.data_services.post.service.PostService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of posts by friends",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = PostDTO.class)),
                                    mediaType = "application/json"
                            )
                    }),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of posts created by user",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = PostDTO.class)),
                                    mediaType = "application/json"
                            )
                    }),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/posts-posted-by-user")
    public ResponseEntity<Object> getPostsCreatedByUser(
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of posts created by user",
                    content = {
                            @Content(schema = @Schema(implementation = PostDTO.class),
                                    mediaType = "application/json"
                            )
                    }),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted",
                    content = {
                            @Content(array = @ArraySchema(schema = @Schema(implementation = PostDTO.class)),
                                    mediaType = "application/json"
                            )
                    }),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
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