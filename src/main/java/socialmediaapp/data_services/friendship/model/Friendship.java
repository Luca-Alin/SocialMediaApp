package socialmediaapp.data_services.friendship.model;

import jakarta.persistence.*;
import lombok.*;
import socialmediaapp.data_services.user.model.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "friendship")
@IdClass(FriendshipId.class)
public class Friendship {

    @Id
    @ManyToOne
    private User sender;

    @Id
    @ManyToOne
    private User receiver;

    @Column(nullable = false)
    private FriendshipStatus friendshipStatus;


    /**
     * @param object Friendship
     * @return Will return true if comparing Friendship1 [friend1 -> friend2] and
     * Friendship2 [friendship2 -> friendship1], and false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Friendship that = (Friendship) object;

        String s1 = this.sender.getUuid();
        String r1 = this.receiver.getUuid();

        String s2 = that.sender.getUuid();
        String r2 = that.receiver.getUuid();

        return s1.equals(s2) && r1.equals(r2) ||
               s1.equals(r2) && r1.equals(s2);
    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (friendshipStatus != null ? friendshipStatus.hashCode() : 0);
        return result;
    }
}