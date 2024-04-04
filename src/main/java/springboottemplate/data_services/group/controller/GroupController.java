package springboottemplate.data_services.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.group.model.Group;
import springboottemplate.data_services.group.model.enums.HowToJoin;
import springboottemplate.data_services.group.model.enums.WhoCanCreatePosts;
import springboottemplate.data_services.group.model.enums.WhoCanSeePosts;
import springboottemplate.data_services.group.service.GroupService;
import springboottemplate.data_services.post.model.PostDTO;

import java.util.List;

@RequiredArgsConstructor

@RequestMapping("/api/v1/group")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{uuid}")
    public ResponseEntity<List<PostDTO>> findGroupById(
            @PathVariable String uuid
    ) {
        return null;
    }

    @GetMapping("/{uuid}/posts")
    public ResponseEntity<List<PostDTO>> findGroupPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String uuid,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    ) {
        return null;
    }

    @PostMapping("/")
    public void createGroup(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Group group) {
    }

    @PostMapping("/who-can-create-posts")
    public WhoCanCreatePosts setWhoCanCreatePosts(@AuthenticationPrincipal UserDetails userDetails, @RequestBody WhoCanCreatePosts whoCanCreatePosts) {
        return null;
    }

    @PostMapping("/who-can-see-posts")
    public WhoCanSeePosts setWhoCanSeePosts(@AuthenticationPrincipal UserDetails userDetails, @RequestBody WhoCanSeePosts whoCanSeePosts) {
        return null;
    }

    @PostMapping("/how-to-join")
    public HowToJoin setHowToJoin(@AuthenticationPrincipal UserDetails userDetails, @RequestBody HowToJoin howToJoin) {
        return null;
    }
}
