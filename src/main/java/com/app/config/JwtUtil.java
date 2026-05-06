package com.app.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.app.constant.ErrorCodes;
import com.app.exception.UnauthorizedException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;

    public String generateToken(String email,String role,String fullName, String officialRole){
        try {
            JWSSigner signer=new MACSigner(jwtConfig.getSecret());

            JWTClaimsSet claimsSet=new JWTClaimsSet.Builder()
                                        .subject(email)
                                        .claim("role", role)
                                        .claim("name", fullName)
                                        .claim("officialRole", officialRole)
                                        .issueTime(new Date())
                                        .expirationTime(new Date(System.currentTimeMillis()+jwtConfig.getExpiration()))
                                        .build();
                                        
            SignedJWT signedJWT=new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("JWT generation Failed");
        }

    }

    public JWTClaimsSet validateToken(String token){
        try {
            SignedJWT signedJWT=SignedJWT.parse(token);

            JWSVerifier verifier=new MACVerifier(jwtConfig.getSecret().getBytes());

            if(!signedJWT.verify(verifier)){
                throw new UnauthorizedException("Invalid token");
            }

            JWTClaimsSet claims=signedJWT.getJWTClaimsSet();

             if (claims.getExpirationTime().before(new Date())) {
                throw new UnauthorizedException("Token expired");
            }

            return claims;



        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }


}