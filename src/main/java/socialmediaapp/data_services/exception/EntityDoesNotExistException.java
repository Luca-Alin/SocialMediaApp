package socialmediaapp.data_services.exception;

import java.util.Arrays;

public class EntityDoesNotExistException extends Exception {
    public EntityDoesNotExistException(Class<?> clazz) {
        super("The entity %s does not exist".formatted(
                Arrays.stream(clazz.getName().split("\\."))
                        .reduce((first, second) -> second)
                        .orElseThrow()
        ));
    }
}
