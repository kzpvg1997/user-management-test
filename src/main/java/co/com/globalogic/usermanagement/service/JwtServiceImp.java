package co.com.globalogic.usermanagement.service;


import co.com.globalogic.usermanagement.domain.exception.ErrorException;
import co.com.globalogic.usermanagement.domain.exception.ExceptionMessage;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.UserAuthentication;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

import static co.com.globalogic.usermanagement.service.utils.JwtUtils.NAME_CLAIM;
import static co.com.globalogic.usermanagement.service.utils.JwtUtils.EMAIL_CLAIM;
import static co.com.globalogic.usermanagement.service.utils.JwtUtils.LAST_LOGIN_CLAIM;
import static co.com.globalogic.usermanagement.service.utils.JwtUtils.PASSWORD_CLAIM;

@Service
public class JwtServiceImp {

    private static final String SECRET = "my-super-secret-key-that-is-at-least-32-chars";

    public UserAuthentication generateUserAuthentication(User user){
        // Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        LocalDateTime lastLogin = LocalDateTime.now();

        // Claims
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .claim(NAME_CLAIM, user.getName())
                .claim(EMAIL_CLAIM, user.getEmail())
                .claim(PASSWORD_CLAIM, user.getPassword())
                .claim(LAST_LOGIN_CLAIM, lastLogin.toString())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600_000)) // 1h
                .build();

        // Build Token
        SignedJWT signedJWT = new SignedJWT(header, claims);
        JWSSigner signer;

        // Sing
        try {
            signer = new MACSigner(SECRET.getBytes());
            signedJWT.sign(signer);

        } catch (JOSEException ex) {
            throw new ErrorException(ExceptionMessage.SERVER_ERROR,ex);
        }

        return UserAuthentication.builder()
                .token(signedJWT.serialize())
                .lastLogin(lastLogin)
                .build();
    }

    public User decodeUserToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            // Validate signature
            boolean isValid = signedJWT.verify(new MACVerifier(SECRET.getBytes()));
            // Token Invalid
            if (!isValid) {
                throw new ErrorException(ExceptionMessage.INVALID_TOKEN);
            }

            String subject = signedJWT.getJWTClaimsSet().getSubject();
            String email = signedJWT.getJWTClaimsSet().getStringClaim(EMAIL_CLAIM);
            String name = signedJWT.getJWTClaimsSet().getStringClaim(NAME_CLAIM);

            return User.builder()
                    .id(subject)
                    .name(name)
                    .email(email)
                    .build();

        } catch (ParseException | JOSEException ex) {
            throw new ErrorException(ExceptionMessage.SERVER_ERROR,ex);
        }
    }

}
