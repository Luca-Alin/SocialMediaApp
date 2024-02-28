package springboottemplate.data_services.chat.model;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;
import springboottemplate.data_services.user.model.User;

import java.net.Socket;

/**
 * Note: the equals and hashcode rely only on the userId field
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocketUserCombo {
    private WebSocketSession socket;
    private String userId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SocketUserCombo that = (SocketUserCombo) object;

        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
