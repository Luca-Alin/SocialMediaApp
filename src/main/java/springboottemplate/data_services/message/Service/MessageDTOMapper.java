package springboottemplate.data_services.message.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.message.Message;
import springboottemplate.data_services.message.MessageDTO;
import springboottemplate.data_services.user.UserDTO;
import springboottemplate.data_services.user.service.UserDTOMapper;

import java.util.function.Function;

@RequiredArgsConstructor

@Service
public class MessageDTOMapper implements Function<Message, MessageDTO> {

    private final UserDTOMapper userDTOMapper;
    @Override
    public MessageDTO apply(Message message) {
        UserDTO sendingUser = userDTOMapper.apply(message.getSendingUser());
        UserDTO receiving = userDTOMapper.apply(message.getReceivingUser());

        return MessageDTO
                .builder()
                .id(message.getId())
                .content(message.getContent())
                .sendingUser(sendingUser)
                .receivingUser(receiving)
                .build();
    }
}
