package socialmediaapp.data_services.exception;

public class InvalidEmailException extends Exception{
    public InvalidEmailException(String email) {
        super("Invalid email address: %s".formatted(email));
    }
}
