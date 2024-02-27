package springboottemplate.data_services.friendship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.exception.EntityShouldContainAnId;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.friendship.Friendship;
import springboottemplate.data_services.friendship.FriendshipDTO;
import springboottemplate.data_services.friendship.FriendshipStatus;
import springboottemplate.data_services.friendship.service.FriendshipService;
import springboottemplate.data_services.user.User;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/friendship")
public class FriendshipController {
    private final FriendshipService friendshipService;

    @PostMapping("/send-friendship-request")
    private FriendshipStatus sendFriendshipRequest(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
        try {
            return friendshipService.sendFriendshipRequest(userDetails, user);
        } catch (Exception e) {
            System.out.println(e);
            return FriendshipStatus.PENDING;
        }
    }

    @PostMapping("/friendship-status")
    private FriendshipStatus friendshipStatus(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) throws UserNotFoundException {
        return friendshipService.checkFriendshipStatus(userDetails, user);
    }

    @PostMapping("/accept-friendship")
    public void acceptFriendshipRequest(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) throws UserNotFoundException, EntityShouldContainAnId {
        friendshipService.acceptFriendshipRequest(userDetails, user);
    }

    @GetMapping("/get-received-friendship-requests")
    public List<FriendshipDTO> getReceivedFriendshipRequests(@AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        return friendshipService.getReceivedFriendshipRequests(userDetails);
    }

    @GetMapping("/get-users-friends")
    public List<FriendshipDTO> getUsersFriendships(@AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        return friendshipService.getUsersFriendships(userDetails);
    }
}
