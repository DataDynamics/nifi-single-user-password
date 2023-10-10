package io.datadynamics.nifi.single.user;

public class PasswordGenerator {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private static final int MINIMUM_USERNAME_LENGTH = 4;

    private static final int MINIMUM_PASSWORD_LENGTH = 12;

    public static void main(final String[] arguments) {
        if (arguments.length == 1) {
            final String password = arguments[0];
            if (password.length() < MINIMUM_PASSWORD_LENGTH) {
                System.err.printf("ERROR: Password must be at least %d characters%n", MINIMUM_PASSWORD_LENGTH);
            } else {
                final String encodedPassword = PASSWORD_ENCODER.encode(password.toCharArray());
                System.out.println("Encoded Password : " + encodedPassword);
            }
        } else {
            System.err.printf("Unexpected number of arguments [%d]%n", arguments.length);
            System.err.printf("Usage: %s <password>%n", PasswordGenerator.class.getSimpleName());
        }
    }
}
