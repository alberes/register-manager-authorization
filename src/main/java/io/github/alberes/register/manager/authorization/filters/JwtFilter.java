package io.github.alberes.register.manager.authorization.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.alberes.register.manager.authorization.constants.Constants;
import io.github.alberes.register.manager.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.security.ManagerRegisterUsernamePasswordAuthenticationToken;
import io.github.alberes.register.manager.authorization.services.JWTService;
import io.github.alberes.register.manager.authorization.services.UserService;
import io.github.alberes.register.manager.authorization.services.exceptions.AuthorizationException;
import io.github.alberes.register.manager.authorization.utils.ControllerUtils;
import io.github.alberes.register.manager.authorization.utils.EncryptUtils;
import io.github.alberes.register.manager.authorization.utils.JsonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Order(2)
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final ApplicationContext applicationContext;

    private final EncryptUtils encryptUtils;

    private final ControllerUtils controllerUtils;

    private JsonUtils jsonUtils = new JsonUtils(new ObjectMapper()
            .registerModule(new JavaTimeModule()));

    //Validate token and set UserDetails(Principal)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authentication = request.getHeader(Constants.AUTHORIZATION);
        String token = null;
        String username = null;
        StandardErrorDto standardErrorDto = null;

        //Extract token and validate
        if (authentication != null && authentication.startsWith(Constants.BEARER)) {
            token = authentication.substring(7);
            try {
                username = this.jwtService.extractUsername(token);
            } catch (Exception e) {
                RuntimeException re = new RuntimeException(e);
                log.error(e.getMessage(), e);
                throw re;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserAccount userAccount = this.applicationContext.getBean(UserService.class)
                    .loadUserByUsername(username);
            List<String> origins = this.controllerUtils.origins(username, request);
            String id = null;
            if(this.controllerUtils.hasRoleAdmin(userAccount.getRoles())
                || Constants.API_V1_USERS_AUTHENTICATED.equals(request.getRequestURI())){
                id = userAccount.getId().toString();
            }else {
                id = this.controllerUtils.extractUserId(request.getRequestURI());
                if("".equals(id)){
                    id = userAccount.getId().toString();
                }
            }
            origins.add(id);
            String fingerprint = this.jwtService.generateEncryptOrigins(origins);
            try {
                //Validate token and permission where user can only access your resource
                if (this.jwtService.validateToken(token, userAccount, fingerprint)) {
                    ManagerRegisterUsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new ManagerRegisterUsernamePasswordAuthenticationToken(userAccount, null,
                                    this.controllerUtils.toAuthorities(userAccount.getRoles()));
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }catch(AuthorizationException authorizationException){
                log.error(authorizationException.getMessage());
                standardErrorDto =
                        new StandardErrorDto(System.currentTimeMillis(),
                                authorizationException.getStatus(),
                                HttpStatus.FORBIDDEN.getReasonPhrase(), authorizationException.getMessage(),
                                request.getRequestURI(),
                                List.of()
                        );
                String json = this.jsonUtils.toJson(standardErrorDto);
                log.error(json);
                response.setStatus(standardErrorDto.getStatus());
                response.setCharacterEncoding(Constants.UTF_8);
                response.setContentType(Constants.APPLICATION_JSON);
                response.getWriter().println(json);
            }
        }
        if(standardErrorDto == null) {
            filterChain.doFilter(request, response);
        }
    }

}
