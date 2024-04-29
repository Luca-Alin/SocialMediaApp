package springboottemplate.data_services.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import springboottemplate.data_services.chat.service.ChatService;
import springboottemplate.data_services.message.model.MessageDTO;
import springboottemplate.data_services.message.model.MessageRequest;
import springboottemplate.data_services.message.service.MessageService;
import springboottemplate.data_services.user.model.User;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final MessageService messageService;

    private final Map<String, WebSocketSession> userIds = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> sessions = new ConcurrentHashMap<>();

    private final ChatService chatService;
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = chatService.extractUserFromWebSocketUri(session)
                .orElseThrow(Exception::new);

        String userId = user.getUuid();

        userIds.put(userId, session);
        sessions.put(session, userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = sessions.remove(session);
        this.userIds.remove(userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MessageRequest messageRequest = mapper.readValue(message.getPayload(), MessageRequest.class);

        String senderId = sessions.get(session);
        String receiverId = messageRequest.getReceiverId();

        WebSocketSession receiver = userIds.get(receiverId);

        //send typing notification
        if (messageRequest.getContent() == null) {
            MessageDTO messageDTO = MessageDTO
                    .builder()
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .content(null)
                    .build();
            String json = mapper.writeValueAsString(messageDTO);
            if (receiver != null)
                receiver.sendMessage(new TextMessage(json));

            return;
        }

        //send actual message
        MessageDTO messageDTO = messageService.sendMessage(senderId, messageRequest);
        String json = mapper.writeValueAsString(messageDTO);

        session.sendMessage(new TextMessage(json));
        if (receiver != null)
            receiver.sendMessage(new TextMessage(json));
    }
}
