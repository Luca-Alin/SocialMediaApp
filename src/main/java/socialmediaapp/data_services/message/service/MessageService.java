package springboottemplate.data_services.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.UserNotFoundException;
import springboottemplate.data_services.friendship.repository.FriendshipRepository;
import springboottemplate.data_services.message.UserAreNotFriendsException;
import springboottemplate.data_services.message.model.Conversation;
import springboottemplate.data_services.message.model.Message;
import springboottemplate.data_services.message.model.MessageDTO;
import springboottemplate.data_services.message.model.MessageRequest;
import springboottemplate.data_services.message.repository.MessageRepository;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.model.UserDTO;
import springboottemplate.data_services.user.repository.UserRepository;
import springboottemplate.data_services.user.service.UserDTOMapper;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageDTOMapper messageDTOMapper;
    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final FriendshipRepository friendshipRepository;

    public List<Conversation> findAllConversationsOfUserDetails(UserDetails userDetails) throws UserNotFoundException {
        User user = getUser(userDetails);

        // friend + messages between user and friend map
        Map<User, List<Message>> map = messageRepository.findMessagesSendOrReceivedByUser(user)
                .stream()
                .collect(Collectors.groupingBy(
                        (message) -> user.equals(message.getSender())
                                ? message.getReceiver()
                                : message.getSender()
                ));

        // also add the friends with whom the user doesn't have conversations yet
        friendshipRepository.findCurrentFriendships(user)
                .stream()
                .map(
                        (friendship) -> user.equals(friendship.getSender())
                                ? friendship.getReceiver()
                                : friendship.getSender()
                )
                .forEach((friend) -> map.putIfAbsent(friend, new ArrayList<>()));

        return map.entrySet()
                .stream()
                .map((entry) -> {
                    UserDTO userDTO = userDTOMapper.apply(entry.getKey());
                    List<MessageDTO> messageDTOs = entry.getValue()
                            .stream()
                            .map(messageDTOMapper)
                            .toList();

                    return new Conversation(userDTO, messageDTOs);
                })
                .sorted((a, b) -> {
                    if (a.getMessages().isEmpty()) {
                        return -1;
                    } else if (b.getMessages().isEmpty()) {
                        return 1;
                    }
                    long aTime = a.getMessages().getLast().getDateSent().getTime();
                    long bTime = b.getMessages().getLast().getDateSent().getTime();

                    return -1 * Long.compare(aTime, bTime);
                })
                .toList();
    }


    public MessageDTO sendMessage(UserDetails userDetails, MessageRequest messageRequest) throws UserNotFoundException, UserAreNotFriendsException {
        User sender = getUser(userDetails);
        User receiver = getUser(messageRequest.getReceiverId());

        Message savedMessage = messageRepository.save(
                Message.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .content(messageRequest.getContent())
                        .dateSent(new Date())
                        .build()
        );

        return messageDTOMapper.apply(savedMessage);
    }

    public User getUser(UserDetails userDetails) throws UserNotFoundException {
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);
    }

    public void readMessage(UserDetails userDetails, User sender) throws UserNotFoundException {
        User receiver = getUser(userDetails);
        sender = getUser(sender.getUuid());
        messageRepository.setMessagesRead(receiver, sender);
    }

    public User getUser(String id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
