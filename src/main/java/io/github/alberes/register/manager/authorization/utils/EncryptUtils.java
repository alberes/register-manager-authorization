package io.github.alberes.register.manager.authorization.utils;

import io.github.alberes.register.manager.authorization.constants.Constants;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Slf4j
public class EncryptUtils {

    public String encrypt(String data, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(Constants.AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encryptedData, SecretKey secretKey) {
        try{
            Cipher cipher = Cipher.getInstance(Constants.AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public SecretKey getSecretKey(String keyType, String key) {
        if(Constants.HMACSHA256.equals(keyType)) {
            byte[] keyBytes = Decoders.BASE64.decode(key);
            return Keys.hmacShaKeyFor(keyBytes);
        }else{
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance(keyType);
                keyGen.init(128); // 128 bits
                return keyGen.generateKey();
            } catch (NoSuchAlgorithmException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    public String generateKey(String keyType) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyType);
        SecretKey sk = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(sk.getEncoded());
    }
}
