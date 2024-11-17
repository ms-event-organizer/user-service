package meetup.user_service.user.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordUtilsTest {
    private PasswordUtils passwordUtils;

    @BeforeEach
    void setUp() {
        passwordUtils = new PasswordUtils();
    }

    @Test
    void testHashPassword() {
        String password = "StrongP@ss1";
        String hashedPassword = passwordUtils.hashPassword(password);
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
    void testVerifyPassword_Success() {
        String password = "StrongP@ss1";
        String hashedPassword = passwordUtils.hashPassword(password);
        assertTrue(passwordUtils.verifyPassword(password, hashedPassword));
    }

    @Test
    void testVerifyPassword_Failure() {
        String password = "StrongP@ss1";
        String wrongPassword = "WrongPassword1";
        String hashedPassword = passwordUtils.hashPassword(password);
        assertFalse(passwordUtils.verifyPassword(wrongPassword, hashedPassword));
    }

    @Test
    void testIsPasswordStrong_ValidPassword() {
        String password = "StrongP@ss1";
        assertTrue(passwordUtils.isPasswordStrong(password));
    }

    @Test
    void testIsPasswordStrong_MissingUppercase() {
        String password = "weakp@ss1";
        assertFalse(passwordUtils.isPasswordStrong(password));
    }

    @Test
    void testIsPasswordStrong_MissingLowercase() {
        String password = "WEAKP@SS1";
        assertFalse(passwordUtils.isPasswordStrong(password));
    }

    @Test
    void testIsPasswordStrong_MissingDigit() {
        String password = "WeakPassword!";
        assertFalse(passwordUtils.isPasswordStrong(password));
    }

    @Test
    void testIsPasswordStrong_MissingSpecialCharacter() {
        String password = "WeakP1ssword";
        assertFalse(passwordUtils.isPasswordStrong(password));
    }

    @Test
    void testIsPasswordStrong_TooShort() {
        String password = "Short1!";
        assertFalse(passwordUtils.isPasswordStrong(password));
    }

    @Test
    void testIsPasswordStrong_NullPassword() {
        String password = null;
        assertFalse(passwordUtils.isPasswordStrong(password));
    }

    @Test
    void testIsPasswordStrong_EmptyPassword() {
        String password = "";
        assertFalse(passwordUtils.isPasswordStrong(password));
    }
}
