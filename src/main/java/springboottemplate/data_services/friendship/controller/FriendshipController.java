package springboottemplate.data_services.friendship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import springboottemplate.data_services.friendship.FriendshipService;

@RequiredArgsConstructor

@RestController
public class FriendshipController {
    private final FriendshipService friendshipService;
}
