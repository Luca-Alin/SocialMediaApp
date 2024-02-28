package springboottemplate.data_services.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;
import springboottemplate.data_services.chat.model.SocketUserCombo;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.message.repository.MessageRepository;
import springboottemplate.data_services.user.repository.UserRepository;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final Set<SocketUserCombo> sockets = new HashSet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            SocketUserCombo socket = buildSocketUserCombo(session);

            userRepository.findById(socket.getUserId()).orElseThrow(() -> new Exception("User does not exists"));

            sockets.add(socket);
        } catch (Exception e) {
            session.sendMessage(new TextMessage(e.getMessage()));
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SocketUserCombo socket = buildSocketUserCombo(session);
        sockets.remove(socket);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        try {
            Message message = objectMapper.readValue(textMessage.getPayload(), Message.class);
            Message savedMessage = messageRepository.save(message);
            String savedMessageString  = objectMapper.writeValueAsString(savedMessage);

            //send message back to the user
            session.sendMessage(new TextMessage(savedMessageString));
            //send message to the receiver
            sockets.stream()
                    .filter(suc -> suc.getUserId().equals(savedMessage.getReceiverId()))
                    .findFirst()
                    .orElseThrow()
                    .getSocket()
                    .sendMessage(new TextMessage(savedMessageString));

        } catch (Exception e) {
            session.sendMessage(new TextMessage(e.getMessage()));
        }
    }

    private SocketUserCombo buildSocketUserCombo(WebSocketSession session) {
        URI uri = session.getUri();
        String userId = extractUsername(uri);

        return SocketUserCombo
                .builder()
                .socket(session)
                .userId(userId)
                .build();
    }
    private String extractUsername(URI uri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);
        return builder.build().getQueryParams().getFirst("userid");
    }
}
