package socialmediaapp.data_services.user.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String email) {
        super("User with email %s not found".formatted(email));
    }
}
