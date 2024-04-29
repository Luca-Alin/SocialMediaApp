package springboottemplate.data_services.chat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;
import springboottemplate.data_services.chat.service.ChatService;
import springboottemplate.data_services.message.service.MessageService;
import springboottemplate.data_services.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatWebSocketHandlerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private ChatService chatService;

    private ChatWebSocketHandler underTest;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void ChatWebSocketHandler_afterConnectionEstablished_throwsNoExceptionIfTheUserExists() {
        //Arrange
        WebSocketSession mockWebSocket = mock();
        User mockUser = mock();
        String mockId = "";

        underTest = new ChatWebSocketHandler(messageService, chatService);

        when(chatService.extractUserFromWebSocketUri(mockWebSocket))
                .thenReturn(Optional.of(mockUser));
        when(mockUser.getUuid())
                .thenReturn(mockId);

        //Act and assert
        assertThatNoException().isThrownBy(() -> underTest.afterConnectionEstablished(mockWebSocket));
    }

    @Test
    void ChatWebSocketHandler_afterConnectionEstablished_throwsExceptionIfTheUserIsAbsent() {
        //Arrange
        WebSocketSession mockWebSocket = mock();

        underTest = new ChatWebSocketHandler(messageService, chatService);

        when(chatService.extractUserFromWebSocketUri(mockWebSocket))
                .thenReturn(Optional.empty());

        //Act and assert
        assertThatException().isThrownBy(() -> underTest.afterConnectionEstablished(mockWebSocket));
    }

    @Test
    void handleTextMessage() {


    }

    @Test
    void afterConnectionClosed() {
    }
}