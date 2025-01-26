package org.example.serveur.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private static final Key SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256); // Clé sécurisée
    private static final long EXPIRATION_TIME = 1800000; // 30 minutes


    // Générer un token
    public String generateToken(String senserId) {
        return Jwts.builder()
                .setSubject(senserId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String validateTokenAndGetEmail(String token) {
        try {
            // Parser le token et récupérer les claims
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // Vérifier si le token est expiré
            if (claims.getExpiration().before(new Date())) {
                return null; // Retourner null si le token est expiré
            }

            // Retourner l'email (le "subject" du token)
            return claims.getSubject();
        } catch (Exception e) {
            // En cas d'erreur (token invalide ou autre), retourner null
            return null;
        }

    }


}
