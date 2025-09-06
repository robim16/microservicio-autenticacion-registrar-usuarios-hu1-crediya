package co.com.crediya.jwttokenserviceadapter;

import co.com.crediya.model.usuario.security.AuthenticatedUser;
import co.com.crediya.model.usuario.security.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenServiceAdapter implements TokenService {

    private final Key privateKey;
    private final Key publicKey;
    private final long expiration;

    public JwtTokenServiceAdapter(KeyLoader keyLoader, JwtProperties jwtProperties) {
        try {
            this.privateKey = keyLoader.loadPrivateKey(jwtProperties.getKeys().getPrivateKey());
            this.publicKey = keyLoader.loadPublicKey(jwtProperties.getKeys().getPublicKey());
            this.expiration = jwtProperties.getExpiration();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading JWT keys", e);
        }
    }

    @Override
    public String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public AuthenticatedUser validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        return new AuthenticatedUser(email, role);
    }


}
