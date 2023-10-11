import io.jsonwebtoken.JwtException;

import static org.junit.Assert.*;

public class UserValidatorTest {

    @org.junit.Test
    public void testValidToken() {
        String username = "testUser";
        String email = "test@example.com";

        String token = UserValidator.generateJWT(username, email);

        assert (!token.isEmpty());

        String verificationResult = UserValidator.verifyJWT(token);

        assertEquals("verification pass", verificationResult);
    }

    @org.junit.Test
    public void testInvalidToken() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"; // Replace with an invalid token

        String verificationResult = UserValidator.verifyJWT(invalidToken);

        assertEquals("verification fails", verificationResult);
    }

}