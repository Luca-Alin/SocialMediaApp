package springboottemplate.data_services.chat.controller;

import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import springboottemplate.data_services.chat.model.Message;

@RequiredArgsConstructor

//@Controller
public class ChatController {
//
//    private final SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public Message receiveMessage(@Payload Message message) {
//        return message;
//    }
//
//    @MessageMapping("/private-message")
//    public Message recMessage(@Payload Message message) {
//        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
//        System.out.println(message);
//        return message;
//    }
}
