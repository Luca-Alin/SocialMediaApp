package springboottemplate.data_services.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String email) {
        super("User with email %s not found".formatted(email));
    }
}
