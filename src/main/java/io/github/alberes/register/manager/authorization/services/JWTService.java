package io.github.alberes.register.manager.authorization.services;

import io.github.alberes.register.manager.authorization.constants.Constants;
import io.github.alberes.register.manager.authorization.controllers.dto.TokenDto;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.services.exceptions.AuthorizationException;
import io.github.alberes.register.manager.authorization.utils.EncryptUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JWTService {

    private SecretKey hmacSecretKey;

    private SecretKey encryptSecretKey;

    private final EncryptUtils encryptUtils;

    @Value("${app.session.expirationtime}")
    private int sessionExpiration;

    @Autowired
    private DateTimeFormatter formatter;

    public JWTService(EncryptUtils encryptUtils) throws NoSuchAlgorithmException {
        this.encryptUtils = encryptUtils;
        String key = this.encryptUtils.generateKey(Constants.HMACSHA256);
        this.hmacSecretKey = this.encryptUtils.getSecretKey(Constants.HMACSHA256, key);
        this.encryptSecretKey = this.encryptUtils.getSecretKey(Constants.AES, null);
    }

    public TokenDto generateToken(UserAccount userAccount, List<String> origins) throws Exception {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(Constants.ID, userAccount.getId());
        claims.put(Constants.NAME, userAccount.getName());
        claims.put(Constants.EMAIL, userAccount.getEmail());
        claims.put(Constants.PROFILES, userAccount.getRoles());
        claims.put(Constants.REGISTRATION_DATE, userAccount.getCreatedDate().format(this.formatter));
        claims.put(Constants.FINGER_PRINT, this.generateEncryptOrigins(origins));
        Date startDate = new Date(System.currentTimeMillis());
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(this.sessionExpiration);
        Date expiration = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());
        return new TokenDto(Jwts.builder()
                .claims()
                .add(claims)
                .subject(userAccount.getEmail())
                .issuedAt(startDate)
                .expiration(expiration)
                .and()
                .signWith(this.hmacSecretKey)
                .compact(), expiration.getTime());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractAttribute(String token, String attribute) {
        final Claims claims = extractAllClaims(token);
        return claims.get(attribute, String.class);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.hmacSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserAccount userAccount, String fingerprint) {
        if(!this.extractAttribute(token, Constants.FINGER_PRINT).equals(fingerprint)){
            AuthorizationException authorizationException = new AuthorizationException(HttpStatus.FORBIDDEN.value(),
                    Constants.INVALID_TOKEN);
            log.error(authorizationException.getMessage(), authorizationException);
            throw authorizationException;
        }
        final String userName = extractUsername(token);
        boolean usernameValid = userName.equals(userAccount.getEmail());
        boolean tokenIsNotExpired = !isTokenExpired(token);
        if(!usernameValid){
            log.error("Invalid username: {}", usernameValid);
        }
        if(!tokenIsNotExpired){
            log.error("Token expired for username: {}", userName);
        }
        return usernameValid && tokenIsNotExpired;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateEncryptOrigins(List<String> origins) {
        return this.encryptUtils.encrypt(String.join("", origins), this.encryptSecretKey);
    }
}