package springboottemplate.data_services.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import springboottemplate.data_services.chat.service.ChatService;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.message.model.MessageDTO;
import springboottemplate.data_services.message.model.MessageRequest;
import springboottemplate.data_services.message.service.MessageService;
import springboottemplate.data_services.user.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final MessageService messageService;
    private final Map<String, WebSocketSession> map = new HashMap<>();
    private final ChatService chatService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Optional<User> user = chatService.extractUserFromWebSocketUri(session);
        if (user.isEmpty()) throw new Exception();

        map.put(user.get().getUuid(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Optional<User> user = chatService.extractUserFromWebSocketUri(session);
        if (user.isEmpty()) throw new Exception();

        MessageRequest messageRequest = mapper.readValue(message.getPayload(), MessageRequest.class);
        MessageDTO messageDTO = messageService.sendMessage(user.get(), messageRequest);
        String json = mapper.writeValueAsString(messageDTO);

        Optional<WebSocketSession> sender = Optional.of(map.get(messageDTO.getSenderId()));
        Optional<WebSocketSession> receiver = Optional.of(map.get(messageDTO.getReceiverId()));

        sender.ifPresent(u -> {
            try {
                u.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        receiver.ifPresent(u -> {
            try {
                u.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Optional<User> user = chatService.extractUserFromWebSocketUri(session);
        if (user.isEmpty()) throw new Exception();


        map.remove(user.get().getUuid());
    }
}
