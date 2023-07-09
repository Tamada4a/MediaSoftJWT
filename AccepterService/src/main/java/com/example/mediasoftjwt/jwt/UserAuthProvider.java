package com.example.mediasoftjwt.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;


@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    private final RestTemplate restTemplate = new RestTemplate();


    public Authentication validateToken(String token) throws ParseException, JOSEException {
        RSAPublicKey rsaPublicKey = getPublicKey(token);

        JWSVerifier verifier = new RSASSAVerifier(rsaPublicKey);

        SignedJWT signed = SignedJWT.parse(token);

        signed.verify(verifier);

        String email = (String) signed.getJWTClaimsSet().getClaims().get("email");

        return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
    }


    private RSAPublicKey getPublicKey(String token) throws JOSEException, ParseException {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<?> result = restTemplate.exchange("http://localhost:8090/.well-known/jwks.json", HttpMethod.GET, entity, Map.class);

        Map<String, Object> rsaKeyJson = (Map<String, Object>) result.getBody();
        RSAKey rsaKey = RSAKey.parse(rsaKeyJson);

        return rsaKey.toRSAPublicKey();
    }
}
