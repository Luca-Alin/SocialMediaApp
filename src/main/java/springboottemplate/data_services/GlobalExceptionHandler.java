package springboottemplate.data_services;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleAllExceptions(Throwable t) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping("/error")
    public ResponseEntity<?> error() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
