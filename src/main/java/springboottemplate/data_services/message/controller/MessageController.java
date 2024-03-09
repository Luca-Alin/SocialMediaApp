package springboottemplate.data_services.message.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.message.model.Conversation;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.message.service.MessageService;
import springboottemplate.data_services.user.model.UserDTO;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<Object> findAllConversations(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<Conversation> conversations = messageService.findAllConversationsOfUserDetails(userDetails);
            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
    }
}
