package meetup.user_service.user.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
