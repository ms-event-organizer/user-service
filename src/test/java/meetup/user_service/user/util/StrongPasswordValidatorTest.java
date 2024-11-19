package meetup.user_service.user.util;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class StrongPasswordValidatorTest {

    private final StrongPasswordValidator validator = new StrongPasswordValidator();


    @Test
    @DisplayName("Valid password - meets all criteria")
    void validPassword() {
        String password = "Aa1!abcd";
        assertTrue(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Password should be valid");
    }

    @Test
    @DisplayName("Invalid password - too short")
    void invalidPasswordTooShort() {
        String password = "Aa1!";
        assertFalse(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Password with less than 8 characters should be invalid");
    }

    @Test
    @DisplayName("Invalid password - no uppercase letter")
    void invalidPasswordNoUppercase() {
        String password = "aa1!abcd";
        assertFalse(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Password without uppercase letter should be invalid");
    }

    @Test
    @DisplayName("Invalid password - no lowercase letter")
    void invalidPasswordNoLowercase() {
        String password = "AA1!ABCD";
        assertFalse(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Password without lowercase letter should be invalid");
    }

    @Test
    @DisplayName("Invalid password - no digit")
    void invalidPasswordNoDigit() {
        String password = "Aa!bcdef";
        assertFalse(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Password without a digit should be invalid");
    }

    @Test
    @DisplayName("Invalid password - no special character")
    void invalidPasswordNoSpecialCharacter() {
        String password = "Aa1bcdef";
        assertFalse(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Password without a special character should be invalid");
    }

    @Test
    @DisplayName("Invalid password - contains unsupported special character")
    void invalidPasswordUnsupportedSpecialCharacter() {
        String password = "Aa1=abcd";
        assertFalse(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Password with unsupported special character should be invalid");
    }

    @Test
    @DisplayName("Valid password - minimum valid configuration")
    void validPasswordMinimumLength() {
        String password = "Aa1!abcd";
        assertTrue(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Password with minimum valid length and criteria should be valid");
    }

    @Test
    @DisplayName("Valid password - longer password with multiple special characters")
    void validPasswordLonger() {
        String password = "Aa1!Abcdefghij12345!@#";
        assertTrue(validator.isValid(password, mock(ConstraintValidatorContext.class)),
                "Longer password with multiple special characters should be valid");
    }
}