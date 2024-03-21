package springboottemplate.data_services.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.exception.EntityDoesNotExistException;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.UserDoesNotOwnEntityException;
import springboottemplate.data_services.post.model.Post;
import springboottemplate.data_services.post.model.PostDTO;
import springboottemplate.data_services.post.model.PostReaction;
import springboottemplate.data_services.post.model.PostReactionType;
import springboottemplate.data_services.post.service.PostService;
import springboottemplate.data_services.user.exceptions.UserNotFoundException;
import springboottemplate.data_services.user.model.UserDTO;
import springboottemplate.data_services.user.service.UserDTOMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public ResponseEntity<List<PostDTO>> findAllByUserAndFriends(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    ) {
        try {
            List<PostDTO> postDTOS = postService.findAllPostsByUserAndHisFriends(userDetails, PageRequest.of(pageNumber, pageSize));
            return new ResponseEntity<>(postDTOS, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<List<PostDTO>> findByUserId(
            @PathVariable String uuid,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    ) {
        try {
            List<PostDTO> posts = postService.findAllPostsByUserId(uuid, PageRequest.of(pageNumber, pageSize));
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<PostDTO> createPost(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post
    ) {
        try {
            PostDTO postDTO = postService.save(userDetails, post);
            return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    record PostReactionDTO(String userId, PostReactionType postReactionType) {
    }
    @PostMapping("/reaction/{uuid}")
    public ResponseEntity<?> addReaction(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable String uuid, @RequestBody PostReactionType type
    ) {
        try {
            List<PostReaction> postReaction = postService.addReaction(userDetails, uuid, type);
            return ResponseEntity.ok(postReaction.stream().map((pr) -> new PostReactionDTO(pr.getUser().getUuid(), pr.getReaction())).toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<PostDTO> updatePost(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post
    ) {
        try {
            PostDTO postdto = postService.updatePost(userDetails, post);
            return new ResponseEntity<>(postdto, HttpStatus.OK);
        } catch (UserNotFoundException | EntityDoesNotExistException | EntityShouldContainAnId e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UserDoesNotOwnEntityException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable String uuid
    ) {
        try {
            postService.deletePost(userDetails, uuid);
            return new ResponseEntity<>("Post deleted", HttpStatus.OK);
        } catch (EntityDoesNotExistException e) {
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        } catch (UserDoesNotOwnEntityException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User does not exits", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}