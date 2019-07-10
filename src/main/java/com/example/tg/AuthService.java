package com.example.tg;

import com.example.tg.models.User;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

/**
 * Authenticate users.
 * This class should be replaced with modern ready-made authentication solution such as Keycloak.
 * @deprecated
 */
public class AuthService {
    private static final Integer MINIMUM_HEADER_LENGTH = 12; // theoretical 1 letter username and
                                                             // password.
    private static final int USERNAME_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;

    @Inject
    private UserDataRepository userRepo;

    /**
     * Authenticates user. Throws exception if auth failed.
     * @param authHeader header
     * @return on successful authentication, found user
     */
    public User authenticate(String authHeader) {
        // Basic auth is defined in RFC7617: https://tools.ietf.org/html/rfc7617
        // the header is of the form, quotes removed:
        // 'Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='
        // where the base64 data is, quotes removed: 'Aladdin:open sesame'

        if (authHeader == null
                || authHeader.length() < MINIMUM_HEADER_LENGTH
                || authHeader.split(" ").length < 2) {
            throw ResponseFactory.generateNotAuthorizedException();
        }
        String dataBase64 = authHeader.split(" ")[1];

        // usernames and passwords are required to be UTF-8
        String[] decoded =
                new String(Base64.decodeBase64(dataBase64), StandardCharsets.UTF_8).split(":");

        if (decoded.length < 2) {
            throw ResponseFactory.generateNotAuthorizedException();
        }

        Optional<User> user = userRepo.findUser(decoded[USERNAME_INDEX]);

        if (!user.isPresent()) {
            throw ResponseFactory.generateNotAuthorizedException();
        }

        if (!slowCompare(
                Base64.decodeBase64(user.get().getPasswordSaltedHash()),
                hashSha256(decoded[PASSWORD_INDEX] + user.get().getSalt()))) {
            throw ResponseFactory.generateNotAuthorizedException();
        };

        return user.get();
    }

    // prevent timing attack by comparing in constant time
    private boolean slowCompare(byte[] first, byte[] second) {
        if (first.length != second.length) {
            return false;
        }

        int status = 0;
        for (int i = 0; i < first.length; i++) {
            status = status | (first[i] ^ second[i]);
        }

        return status == 0;
    }

    private byte[] hashSha256(String toBeHashed) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = toBeHashed.getBytes();
            return sha256.digest(passBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException();
        }
    }
}
