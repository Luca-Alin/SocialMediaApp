package springboottemplate.data_services.friendship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.exception.EntityDoesNotExistException;
import springboottemplate.data_services.friendship.exception.FriendshipAlreadyExists;
import springboottemplate.data_services.friendship.model.FriendshipStatus;
import springboottemplate.data_services.friendship.service.FriendshipService;
import springboottemplate.data_services.user.exceptions.UserNotFoundException;
import springboottemplate.data_services.user.model.User;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/friendships")
@Slf4j
public class FriendshipController {
    private final FriendshipService friendshipService;

    @PostMapping("/send")
    private ResponseEntity<FriendshipStatus> sendFriendshipRequest(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
        try {
            FriendshipStatus friendshipStatus = friendshipService.sendFriendshipRequest(userDetails, user);
            return new ResponseEntity<>(friendshipStatus, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (FriendshipAlreadyExists e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accept")
    private ResponseEntity<FriendshipStatus> acceptFriendshipRequest(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
        try {
            FriendshipStatus friendshipStatus = friendshipService.acceptFriendshipRequest(userDetails, user);
            return new ResponseEntity<>(friendshipStatus, HttpStatus.OK);
        } catch (UserNotFoundException | EntityDoesNotExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/friendship-status")
    private ResponseEntity<FriendshipStatus> friendshipStatus(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
        try {
            FriendshipStatus friendshipStatus = friendshipService.checkFriendshipStatus(userDetails, user);
            return new ResponseEntity<>(friendshipStatus, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/")
    private ResponseEntity<?> deleteFriendship(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
        try {
            friendshipService.deleteFriendship(userDetails, user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
