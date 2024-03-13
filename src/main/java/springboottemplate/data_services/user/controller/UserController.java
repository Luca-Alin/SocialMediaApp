package springboottemplate.data_services.user.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.user.exceptions.UserNotFoundException;
import springboottemplate.data_services.user.model.UserDTO;
import springboottemplate.data_services.user.service.InvalidSearchQueryException;
import springboottemplate.data_services.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;



    @GetMapping("/public/{uuid}")
    public ResponseEntity<Object> getUserById(@PathVariable String uuid) {
        try {
            return new ResponseEntity<>(userService.findUserById(uuid), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/friends")
    public List<UserDTO> findFriendsByUserId(@AuthenticationPrincipal UserDetails userDetails) {
        throw new RuntimeException();
    }
    @GetMapping("/friend-requests-sent-by-user")
    public List<UserDTO> findFriendRequestsSendByUser(@AuthenticationPrincipal UserDetails userDetails) {
        throw new RuntimeException();
    }
    @GetMapping("/friend-requests-received-by-user")
    public List<UserDTO> findFriendRequestsReceivedByUser(@AuthenticationPrincipal UserDetails userDetails) {
        throw new RuntimeException();
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchUsers(@RequestParam String query) {
        try {
            List<UserDTO> userDTOS = userService.searchUserByName(query);
            return new ResponseEntity<>(userDTOS, HttpStatus.OK);
        } catch (InvalidSearchQueryException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user-by-jwt")
    public ResponseEntity<Object> getUserByUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            return new ResponseEntity<>(userService.findUserByUserDetails(userDetails), HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/")
    public ResponseEntity<UserDTO> patchUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.patchUser(userDetails, userDTO));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String uuid) {
        try {
            userService.deleteUser(userDetails, uuid);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
