package meetup.user_service.user.util;

import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PasswordUtils {
    private final String specialChars = "!@#$%^&*()-+";
    private final String notStrongPasswordText = "Password is not strong! Password: at least 8 characters, " +
            "one uppercase, one lowercase, one digit, and one special character " + specialChars;
    private final String wrongPasswordText = "Wrong password!";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}