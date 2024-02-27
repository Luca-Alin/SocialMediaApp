package springboottemplate.data_services.user.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.user.UserDTO;
import springboottemplate.data_services.user.repository.UserRepository;
import springboottemplate.data_services.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;



    @GetMapping("/public/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        UserDTO userDTO = null;
        try {
            userDTO = userService.findUserById(id);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("JWT token is expired", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
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
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query) {
        List<UserDTO> userDTOS = userService.searchUserByName(query);
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
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

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@AuthenticationPrincipal UserDetails userDetails) {
        throw new RuntimeException();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        throw new RuntimeException();
    }
}
