package com.urbanfleet.api_gateway.utility;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtility {

    @Component
    public class JwtUtil {

        // Same secrete key used to sign the token
        private final String secret = "superSecretKeyChangeThisInProd123!";

        /*
        method does 2 parts
        1. with the secrete key check if it's valid or corrupted
        2.  check if it's valid till expiration
         */
        public void validateToken(String token) {
            Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token);
        }
    }
}
