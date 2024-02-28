package springboottemplate.data_services.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;
import springboottemplate.data_services.chat.model.SocketUserCombo;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.user.repository.UserRepository;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<SocketUserCombo> sockets = new HashSet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;

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

            //send message back to the user
            session.sendMessage(textMessage);

            //send message to the receiver
            sockets.stream()
                    .filter(suc -> suc.getUserId().equals(message.getReceiverId()))
                    .findFirst()
                    .orElseThrow()
                    .getSocket()
                    .sendMessage(textMessage);

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

    @Scheduled(fixedRate = 5000)
    public void displayConnectedUsers() {
        System.out.println("Displaying connected users");
        sockets.forEach(suc -> System.out.println(suc.getSocket() + " " + suc.getUserId()));
        System.out.println();
    }
}
