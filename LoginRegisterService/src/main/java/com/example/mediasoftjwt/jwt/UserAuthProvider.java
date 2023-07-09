package com.example.mediasoftjwt.jwt;

import com.example.mediasoftjwt.helpers.SelfKeyPairGenerator;
import com.example.mediasoftjwt.helpers.services.PlayerService;
import com.example.mediasoftjwt.mysql.interfaces.UserInfoRepository;
import com.example.mediasoftjwt.mysql.tables.UserInfo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Collections;


@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    private RSAPrivateKey rsaPrivateKey;
    private RSAPublicKey rsaPublicKey;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserInfoRepository userInfoRepository;


    @PostConstruct
    protected void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //SelfKeyPairGenerator.generateKeyPair();

        rsaPrivateKey = SelfKeyPairGenerator.getPrivate();
        rsaPublicKey = SelfKeyPairGenerator.getPublic();
    }


    public String createToken(String login, String email) throws JOSEException {

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .claim("email", email)
                .issuer(login)
                .build();

        JWSSigner signer = new RSASSASigner(rsaPrivateKey);

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }


    public Authentication validateToken(String token) throws ParseException, JOSEException {
        JWSVerifier verifier = new RSASSAVerifier(rsaPublicKey);

        SignedJWT signed = SignedJWT.parse(token);

        signed.verify(verifier);

        String email = (String) signed.getJWTClaimsSet().getClaims().get("email");

        UserInfo userInfo = userInfoRepository.findByEmail(email).get(0);

        return new UsernamePasswordAuthenticationToken(userInfo, null, Collections.emptyList());
    }
}
