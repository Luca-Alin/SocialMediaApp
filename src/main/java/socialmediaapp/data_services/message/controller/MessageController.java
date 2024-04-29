package springboottemplate.data_services.message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.message.model.Conversation;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.message.service.MessageService;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.model.UserDTO;

import java.util.List;

@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/")
    public ResponseEntity<Object> findAllConversations(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<Conversation> conversations = messageService.findAllConversationsOfUserDetails(userDetails);
            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/read")
    public ResponseEntity<?> setConversationToRead(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User sender) {
        try {
            messageService.readMessage(userDetails, sender);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
