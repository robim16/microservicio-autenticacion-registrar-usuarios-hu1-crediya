package co.com.crediya.jwttokenserviceadapter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyLoader {

    public PrivateKey loadPrivateKey(String location) throws Exception {
        Resource resource = resolveResource(location);
        String keyPem = Files.readString(resource.getFile().toPath())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(keyPem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public PublicKey loadPublicKey(String location) throws Exception {
        Resource resource = resolveResource(location);
        String keyPem = Files.readString(resource.getFile().toPath())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(keyPem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private Resource resolveResource(String location) {
        if (location.startsWith("classpath:")) {
            return new ClassPathResource(location.substring("classpath:".length()));
        } else if (location.startsWith("file:")) {
            return new FileSystemResource(location.substring("file:".length()));
        } else {
            // por defecto intenta filesystem
            return new FileSystemResource(location);
        }
    }
}
