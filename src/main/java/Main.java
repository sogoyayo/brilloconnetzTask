import java.time.LocalDate;

public class Main {


    public static void main(String[] args) {
        String username = "JohnDoe";
        String email = "john.doe@example.com";
        String password = "StrongPassword1!";

        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

//        UserValidator.ValidationResult validationResult = UserValidator.validateUser(username, email, password, dateOfBirth);
//
//        if (validationResult.isValid()) {
//            String jwtToken = UserValidator.generateJWT(username);
//            System.out.println("JWT Token: " + jwtToken);
//
//            String verificationResult = UserValidator.verifyJWT(jwtToken);
//            System.out.println("JWT Verification: " + verificationResult);
//        } else {
//            System.out.println(validationResult.toString());
//        }
        String jwtToken = UserValidator.generateJWT(username, email);
        System.out.println(jwtToken);
    }
}

