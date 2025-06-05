package io.github.alberes.register.manager.authorization.constants;

import java.util.Set;

public interface Constants {

    public static final String DATE_TIME_FORMATTER_PATTERN = "dd/MM/yyyy HH:mm:ss";

    public static final String ADMIN = "ADMIN";
    public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String HAS_ROLE_ADMIN_USER = "hasRole('ADMIN') || hasRole('USER')";

    public static  final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String UNAUTHORIZED_MESSAGE = "The user can only access resources that belong to him.";
    public static final String AUTHORIZATION_FAILURE = "Authorization failure!";

    public static final String UTF_8 = "UTF-8";
    public static final String APPLICATION_JSON = "application/json";

    public static final String SLASH = "/";

    public static final String HMACSHA256 = "HmacSHA256";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PROFILES = "profiles";
    public static final String REGISTRATION_DATE = "registrationDate";

    public static final String REGISTRATION_WITH_E_MAIL = "Registration with e-mail ";
    public static final String HAS_ALREADY_BEEN_REGISTERED = " has already been registered!";
    public static final String OBJECT_NOT_FOUND_ID = "Object not found! Id: ";
    public static final String TYPE = ", Type: ";
    public static final String OBJECT_NOT_FOUND = "Object not found!";

    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "access-control-expose-headers";
    public static final String LOCATION = "location";

    public static final String AUTHORIZATION_FAILURE_USER = "Authorization Failure, user: ";

    public static final String AES = "AES";

    public static final String FINGER_PRINT = "finger-print";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String USER_AGENT = "User-Agent";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String APP_NAME = "app-name";

    public static final String ASTERISK = "*";
    public static final String SLASH_ASTERISK_ASTERISK = "/**";
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    public static final String API_V1_USERS_AUTHENTICATED = "/api/v1/users/authenticated";

    public static final String API_V1_LOGIN = "/api/v1/login";

    public static final Set<String> SWAGGERS = Set.of("/v2/api-docs/**", "/v3/api-docs/**", "/swagger-resources/**",
            "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/actuator/**");

    public static final String TRUE = "true";
    public static final String NOT_FOUND = "Not found.";
    public static final String ADDRESS_NOT_FOUND = "Address not found.";
    public static final String USER_OR_PASSWORD_INCORRECT = "User or password incorrect!";

    public static final String BLANK = "";

}
