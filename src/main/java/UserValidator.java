import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class UserValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$");
    public static final String SECRET_KEY = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6";

//    public static ValidationResult validateUser(String username, String email, String password, String dateOfBirth) {
//        ValidationResult result = new ValidationResult();
//
//        if (username.isEmpty() || username.length() < 4) {
//            result.addError("Username: not empty, minimum 4 characters");
//        }
//
//        if (!isValidEmail(email)) {
//            result.addError("Email: not empty, valid email address");
//        }
//
//        if (!isValidPassword(password)) {
//            result.addError("Password: not empty, strong password required");
//        }
//
//        if (dateOfBirth == null || !isDateOfBirthValid(dateOfBirth)) {
//            result.addError("Date of Birth: not empty, should be 16 years or greater");
//        }
//
//        return result;
//    }

    private static boolean isValidUsername(String username) {
        return !username.isBlank() && username.length() > 3;
    }

    private static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return !email.isBlank() && matcher.matches();
    }

    private static boolean isValidPassword(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return !password.isBlank() && matcher.matches();
    }

    private static boolean isDateOfBirthValid(String dateOfBirth) {
//        System.out.print("Enter your DOB in YYYY-MM-DD format: ");
        LocalDate today = LocalDate.now();
        if (!dateOfBirth.isBlank()) {
            try {
                LocalDate dob = LocalDate.parse(dateOfBirth);
//                System.out.println("DOB of user is " + dob);
                Period age = Period.between(dob, today);
//                System.out.println(age.getYears());
                return age.getYears() >= 16;
            }catch(Exception e) {
                System.out.println("Invalid Date: " + e.getMessage());
            }
        }
        return false;
    }


    public static String generateJWT(String username, String email) {

        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
//                .setIssuer("brilloconnetz")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("username", username)
                .claim("email", email)
                .signWith(key)
                .compact();

    }

    public static String verifyJWT(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Claims claims = Jwts.parser() .setSigningKey(key).build().parseClaimsJws(token).getBody();
            System.out.println(claims);

            return "verification pass";
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            return "verification fails";
        }
    }

    public static String formatHashMap(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }

        // Remove the trailing ", " from the last entry
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }

//    public static class ValidationResult {
//        private StringBuilder errors = new StringBuilder();
//
//        public void addError(String message) {
//            if (errors.length() > 0) {
//                errors.append(", ");
//            }
//            errors.append(message);
//        }
//
//        public boolean isValid() {
//            return errors.length() == 0;
//        }
//
//        @Override
//        public String toString() {
//            return isValid() ? "Validation passed" : "Validation failed: " + errors.toString();
//        }
//    }

    public static Map<String, String> validateUser(String username, String email, String password, String dateOfBirth) {
        Map<String, String> validationErrors = new HashMap<>();

        if (!isValidUsername(username)) {
            validationErrors.put("Username ", " not empty and should be at least 4 characters");
        }

        if (!isValidEmail(email)) {
            validationErrors.put("Email ", " not empty and should be a valid email address");
        }

        if (!isValidPassword(password)) {
            validationErrors.put("Password ", " not empty, should be a strong password");
        }

        if (!isDateOfBirthValid(dateOfBirth)) {
            validationErrors.put("Date of Birth ", " not empty, should be 16 years or greater and be in YYYY-MM-DD format");
        }
//        System.out.println(validationErrors.size());

        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        return null;

    }

    public static Map<String, String> validateUserAsync(String username, String email, String password, String dateOfBirth) {
        Map<String, String> validationErrors = new HashMap<>();

        CompletableFuture<Void> usernameValidation = CompletableFuture.runAsync(() -> {
//            System.out.println("isValidUsername username is running on " + Thread.currentThread().getName());
            delay(2);
            if (!isValidUsername(username)) {
                validationErrors.put("Username", "not empty and should be at least 4 characters");
            }
        });

        CompletableFuture<Void> emailValidation = CompletableFuture.runAsync(() -> {
//            System.out.println("isValidEmail username is running on " + Thread.currentThread().getName());
            delay(2);
            if (!isValidEmail(email)) {
                validationErrors.put("Email", "not empty and should be a valid email address");
            }
        });

        CompletableFuture<Void> passwordValidation = CompletableFuture.runAsync(() -> {
//            System.out.println("isValidPassword username is running on " + Thread.currentThread().getName());
            delay(2);
            if (!isValidPassword(password)) {
                validationErrors.put("Password", "not empty, should be a strong password");
            }
        });

        CompletableFuture<Void> dateOfBirthValidation = CompletableFuture.runAsync(() -> {
//            System.out.println("isDateOfBirthValid username is running on " + Thread.currentThread().getName());
            delay(2);
            if (!isDateOfBirthValid(dateOfBirth)) {
                validationErrors.put("Date of Birth", "not empty, should be 16 years or greater and be in YYYY-MM-DD format");
            }
        });

        CompletableFuture<Void> allValidations = CompletableFuture.allOf(
                usernameValidation, emailValidation, passwordValidation, dateOfBirthValidation
        );

        allValidations.join();

        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        String jwtToken = generateJWT(username, email);
        Map<String, String> jwt = new HashMap<>();
        jwt.put("JWT", jwtToken);
        return jwt;
    }

    public static void delay(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int numberOfThreads = ForkJoinPool.commonPool().getParallelism();
        System.out.println("Number of common pool threads: " + numberOfThreads);

        String username = "israel";
        String email = "repah15124@gronasu.com";
        String password = "Password@93";
        String dateOfBirth = "2007-03-23";

        System.out.println(validateUser(username, email, password, dateOfBirth));
//        System.out.println(isValidUsername(username));
//        System.out.println(generateJWT(username, email));

        Map<String, String> validationResults = validateUserAsync(username, email, password, dateOfBirth);
        if (validationResults.containsKey("JWT")) {
            System.out.println("JWT Token: " + validationResults.get("JWT"));
        } else {
            System.out.println("Validation errors: " + formatHashMap(validationResults));
        }

        if (validationResults.containsKey("JWT"))
            System.out.println(verifyJWT(validationResults.get("JWT")));

    }

}
