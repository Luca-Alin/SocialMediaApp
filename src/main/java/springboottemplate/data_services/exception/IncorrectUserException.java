package springboottemplate.data_services.exception;

import springboottemplate.data_services.user.model.User;

public class IncorrectUserException extends Exception {
    public IncorrectUserException(User user) {
        super("The user %s does not own this entity".formatted(user.getEmail()));
    }
}
