package springboottemplate.data_services.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;
import springboottemplate.config.JwtService;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class ChatService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @SuppressWarnings("VulnerableCodeUsages")
    public Optional<User> extractUserFromWebSocketUri(WebSocketSession webSocketSession) {
        URI wsUri = webSocketSession.getUri();

        if (wsUri == null)
            return Optional.empty();

        var uri = UriComponentsBuilder.fromUri(wsUri);
        String token = uri.build().getQueryParams().get("username").get(0);
        String email = jwtService.getUserEmail(token);

        return userRepository.findByEmail(email);
    }
}
