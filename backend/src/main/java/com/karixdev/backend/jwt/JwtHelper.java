package com.karixdev.backend.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.karixdev.backend.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
public class JwtHelper {

    private final String issuer;
    private final long tokenExpirationHours;

    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    private final Clock clock;

    public JwtHelper(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.expiration-hours}") long tokenExpirationHours,
            @Value("${jwt.key.public}") RSAPublicKey publicKey,
            @Value("${jwt.key.private}") RSAPrivateKey privateKey,
            Clock clock
    ) {
        algorithm = Algorithm.RSA256(publicKey, privateKey);
        jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        this.issuer = issuer;
        this.tokenExpirationHours = tokenExpirationHours;

        this.clock = clock;
    }

    public String generateToken(UserPrincipal userPrincipal) {
        Instant now = Instant.now(clock);

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userPrincipal.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(tokenExpirationHours, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    private Optional<DecodedJWT> decodeToken(String token) {
        try {
            return Optional.of(jwtVerifier.verify(token));
        } catch (JWTVerificationException e) {
            log.error("invalid token", e);
        }

        return Optional.empty();
    }

    public boolean isTokenValid(String token) {
        return decodeToken(token).isPresent();
    }

    public String getEmailFromToken(String token) {
        Optional<DecodedJWT> optionalJwt = decodeToken(token);

        if (optionalJwt.isEmpty()) {
            throw new IllegalArgumentException("Provided token is not valid");
        }

        return optionalJwt.get().getSubject();
    }
}
