package springboottemplate.data_services.user.service;

public class InvalidSearchQueryException extends Throwable {

    public InvalidSearchQueryException() {
        super("Invalid query");
    }
}
