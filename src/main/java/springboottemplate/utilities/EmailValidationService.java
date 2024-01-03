package springboottemplate.utilities;


import org.springframework.stereotype.Service;
import springboottemplate.data_services.exception.InvalidEmailException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidationService {
    private final Pattern pattern = Pattern.compile("^.+@.+\\..+$");
    public boolean validate(String email)  {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
