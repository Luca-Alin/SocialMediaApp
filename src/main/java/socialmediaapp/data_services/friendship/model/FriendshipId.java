package socialmediaapp.data_services.friendship.model;

import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import socialmediaapp.data_services.user.model.User;

import java.io.Serializable;

@Getter
@Setter
public class FriendshipId implements Serializable {

    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        FriendshipId that = (FriendshipId) object;

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
        return result;
    }
}
