package socialmediaapp.data_services.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String email) {
        super("User with email %s not found".formatted(email));
    }
}
