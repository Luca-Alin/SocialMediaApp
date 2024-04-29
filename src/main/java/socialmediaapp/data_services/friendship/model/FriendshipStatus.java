package springboottemplate.data_services.friendship.model;


public enum FriendshipStatus {
    SENT(0),
    ACCEPTED(1),
    NONE(2);
    public final int index;
    FriendshipStatus(int index){
        this.index = index;
    }
}
