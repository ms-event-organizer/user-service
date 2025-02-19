package meetup.user_service.user.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+_])[A-Za-z\\d!@#$%^&*()\\-+_]{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password.matches(PASSWORD_REGEX);
    }
}