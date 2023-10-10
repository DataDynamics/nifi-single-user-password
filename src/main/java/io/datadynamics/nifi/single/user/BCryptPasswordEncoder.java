package io.datadynamics.nifi.single.user;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Password Encoder implementation using bcrypt hashing
 */
public class BCryptPasswordEncoder implements PasswordEncoder {
    private static final int DEFAULT_COST = 12;

    private static final BCrypt.Version BCRYPT_VERSION = BCrypt.Version.VERSION_2B;

    private static final BCrypt.Hasher HASHER = BCrypt.with(BCRYPT_VERSION);

    private static final BCrypt.Verifyer VERIFYER = BCrypt.verifyer(BCRYPT_VERSION);

    /**
     * Encode Password and return bcrypt hashed password
     *
     * @param password Password
     * @return bcrypt hashed password
     */
    @Override
    public String encode(final char[] password) {
        return HASHER.hashToString(DEFAULT_COST, password);
    }

    /**
     * Match Password against bcrypt hashed password
     *
     * @param password        Password to be matched
     * @param encodedPassword bcrypt hashed password
     * @return Matched status
     */
    @Override
    public boolean matches(final char[] password, final String encodedPassword) {
        final BCrypt.Result result = VERIFYER.verifyStrict(password, encodedPassword.toCharArray());
        return result.verified;
    }
}
