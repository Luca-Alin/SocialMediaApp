package springboottemplate.data_services.message.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.message.Message;
import springboottemplate.data_services.message.Repository.MessageRepository;
import springboottemplate.data_services.user.User;
import springboottemplate.data_services.user.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Map<User, List<Message>> findAllMessageSentAndReceivedByUser(UserDetails userDetails) {
        User user = getUser(userDetails);
        List<Message> messages = messageRepository.findAllBySendingUserOrReceivingUser(user, user);

        

        return messages
                .stream()
                .collect(Collectors.groupingBy((msg) ->
                        Objects.equals(msg.getSendingUser().getId(), user.getId()) ? msg.getReceivingUser() : msg.getSendingUser()
                ));

    }

    public User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();
    }

}
