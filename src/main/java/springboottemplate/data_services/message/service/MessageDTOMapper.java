package springboottemplate.data_services.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.message.model.MessageDTO;
import springboottemplate.data_services.user.service.UserDTOMapper;

import java.util.function.Function;

@RequiredArgsConstructor

@Service
public class MessageDTOMapper implements Function<Message, MessageDTO> {

    @Override
    public MessageDTO apply(Message message) {
        return MessageDTO
                .builder()
                .id(message.getId())
                .senderId(message.getSender().getUuid())
                .receiverId(message.getReceiver().getUuid())
                .content(message.getContent())
                .dateSent(message.getDateSent())
                .build();
    }
}
