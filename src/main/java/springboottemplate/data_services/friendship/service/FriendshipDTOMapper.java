package springboottemplate.data_services.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.friendship.Friendship;
import springboottemplate.data_services.friendship.FriendshipDTO;
import springboottemplate.data_services.user.service.UserDTOMapper;

import java.util.function.Function;

@RequiredArgsConstructor

@Service
public class FriendshipDTOMapper implements Function<Friendship, FriendshipDTO> {
    private final UserDTOMapper userDTOMapper;
    @Override
    public FriendshipDTO apply(Friendship friendship) {
        return FriendshipDTO
                .builder()
                .id(friendship.getId())
                .user(userDTOMapper.apply(friendship.getUser()))
                .friend(userDTOMapper.apply(friendship.getFriend()))
                .friendshipStatus(friendship.getFriendshipStatus())
                .build();
    }
}
