package socialmediaapp.data_services.exception;

import socialmediaapp.data_services.user.model.User;

import java.util.Arrays;

public class UserDoesNotOwnEntityException extends Exception {
    public UserDoesNotOwnEntityException(User user, Class<?> clazz) {
        super("The the user %s does not own this %s".formatted(
                user.getEmail(),
                Arrays.stream(clazz.getName().split("\\."))
                        .reduce((first, second) -> second)
                        .orElseThrow()
        ));
    }
}
