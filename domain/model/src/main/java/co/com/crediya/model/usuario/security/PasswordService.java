package co.com.crediya.model.usuario.security;

public interface PasswordService {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}