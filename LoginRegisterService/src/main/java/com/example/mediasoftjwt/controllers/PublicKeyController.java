package com.example.mediasoftjwt.controllers;

import com.example.mediasoftjwt.helpers.SelfKeyPairGenerator;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Map;


@RestController
public class PublicKeyController {

    @GetMapping("/.well-known/jwks.json")
    public ResponseEntity<Map<String, Object>> getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, ParseException {
        RSAKey jwk = new RSAKey.Builder(SelfKeyPairGenerator.getPublic()).build();

        return ResponseEntity.ok(jwk.toJSONObject());
    }
}
