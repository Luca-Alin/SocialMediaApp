package springboottemplate.data_services.exception;

import java.util.Arrays;

public class EntityShouldNotContainAnId extends Exception {
    public EntityShouldNotContainAnId(Class<?> clazz) {
        super("The entity %s should not contain an id".formatted(
                Arrays.stream(clazz.getName().split("\\."))
                        .reduce((first, second) -> second)
                        .orElseThrow()
        ));
    }
}
