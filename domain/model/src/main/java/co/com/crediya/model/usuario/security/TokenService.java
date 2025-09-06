package co.com.crediya.model.usuario.security;

public interface TokenService {
    String generateToken(String subject, String role);
    AuthenticatedUser validateToken(String token);
}


