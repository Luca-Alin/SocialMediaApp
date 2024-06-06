package socialmediaapp.data_services.exception;

import socialmediaapp.data_services.user.model.User;

public class IncorrectUserException extends Exception {
    public IncorrectUserException(User user) {
        super("The user %s does not own this entity".formatted(user.getEmail()));
    }
}
