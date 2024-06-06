package socialmediaapp.data_services.exception;

import java.util.Arrays;

    public class EntityShouldContainAnId extends Exception {
    public EntityShouldContainAnId(Class<?> clazz) {
        super("The entity %s should contain an id".formatted(
                Arrays.stream(clazz.getName().split("\\."))
                        .reduce((first, second) -> second)
                        .orElseThrow()
        ));
    }
}
