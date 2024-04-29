package springboottemplate.data_services.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.EntityDoesNotExistException;
import springboottemplate.data_services.group.model.Group;
import springboottemplate.data_services.group.model.GroupUser;
import springboottemplate.data_services.group.model.enums.*;
import springboottemplate.data_services.group.repository.GroupRepository;
import springboottemplate.data_services.user.model.User;
import springboottemplate.data_services.user.repository.UserRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public void createGroup(UserDetails userDetails, Group group) {
        User user = getUser(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        byte[] initialGroupImage = group.getGroupImage();
        byte[] resizedGroupImage = resizeImage(initialGroupImage, 300, 300);
        group.setGroupImage(resizedGroupImage);

        GroupUser admin = GroupUser
                .builder()
                .user(user)
                .groupRank(GroupRank.ADMIN)
                .build();

        group.getGroupUsers().add(admin);

        groupRepository.save(group);
    }

    public Group findGroupById(String id) throws EntityDoesNotExistException {
        var group = groupRepository.findGroupById(id);

        if (group.isEmpty())
            throw new EntityDoesNotExistException(Group.class);

        Object[] g = (Object[]) group.get()[0];

        return Group
                .builder()
                .id((String) g[0])
                .groupName((String) g[1])
                .groupDescription((String) g[2])
                .groupImage((byte[]) g[3])
                .whoCanCreatePosts((WhoCanCreatePosts) g[4])
                .whoCanSeePosts((WhoCanSeePosts) g[5])
                .howToJoin((HowToJoin) g[6])
                .build();
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAllGroups()
                .stream()
                .map(obj -> Group
                        .builder()
                        .id((String) obj[0])
                        .groupName((String) obj[1])
                        .groupImage((byte[]) obj[2])
                        .build())
                .toList();
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public static byte[] resizeImage(byte[] originalImageBytes, int targetWidth, int targetHeight) {
        try {
            // Read the byte array as an image
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));

            // Resize the image
            Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            outputImage.getGraphics().drawImage(resizedImage, 0, 0, null);

            // Convert the resized image to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GroupJoinStatus findGroupJoinStatus(UserDetails userDetails, String groupId) {
        User user = getUser(userDetails.getUsername())
                .orElseThrow();

        if (groupRepository.isUserAGroupMember(groupId, user.getUuid())) {
            return GroupJoinStatus.MEMBER;
        } else if (groupRepository.isUserBannedFromGroup(groupId, user.getUuid())) {
            return GroupJoinStatus.BANNED;
        }

        return GroupJoinStatus.NONE;
    }
}
