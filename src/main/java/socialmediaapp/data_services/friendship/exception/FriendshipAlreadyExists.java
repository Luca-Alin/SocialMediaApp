package springboottemplate.data_services.friendship.exception;

public class FriendshipAlreadyExists extends Exception{
    public FriendshipAlreadyExists() {
        super("A pending or accepted friendship already exists between those 2 users");
    }
}
