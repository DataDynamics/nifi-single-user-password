package io.datadynamics.nifi.single.user;

/**
 * Password Encoder for encoding and matching passwords modeled after Spring Security PasswordEncoder
 */
public interface PasswordEncoder {
    /**
     * Encode Password and return hashed password
     *
     * @param password Password
     * @return Encoded Password
     */
    String encode(char[] password);

    /**
     * Match Password against encoded password
     *
     * @param password        Password to be matched
     * @param encodedPassword Encoded representation of password for matching
     * @return Matched status
     */
    boolean matches(char[] password, String encodedPassword);
}
