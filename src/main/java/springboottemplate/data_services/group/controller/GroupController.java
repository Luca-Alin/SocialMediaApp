package springboottemplate.data_services.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.exception.EntityDoesNotExistException;
import springboottemplate.data_services.group.model.Group;
import springboottemplate.data_services.group.model.enums.GroupJoinStatus;
import springboottemplate.data_services.group.model.enums.HowToJoin;
import springboottemplate.data_services.group.model.enums.WhoCanCreatePosts;
import springboottemplate.data_services.group.model.enums.WhoCanSeePosts;
import springboottemplate.data_services.group.service.GroupService;
import springboottemplate.data_services.post.model.PostDTO;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/group-join-status/{groupId}")
    public ResponseEntity<GroupJoinStatus> findGroupJoinStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable  String groupId) {
        return new ResponseEntity<>(groupService.findGroupJoinStatus(userDetails, groupId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Group>> findAllGroups() {
        return new ResponseEntity<>(groupService.findAllGroups(), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Group> findGroupById(
            @PathVariable String uuid
    ) {
        try {
            return new ResponseEntity<>(groupService.findGroupById(uuid), HttpStatus.OK);
        } catch (EntityDoesNotExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<?> createGroup(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Group group) {
        try {
            groupService.createGroup(userDetails, group);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
