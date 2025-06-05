package io.github.alberes.register.manager.authorization.utils;

import io.github.alberes.register.manager.authorization.constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class ControllerUtils {

    public List<String> origins(String username, HttpServletRequest request){
        List<String> list = new ArrayList<String>();
        list.add(username);
        String header = request.getHeader(Constants.X_FORWARDED_FOR);
        if(header == null){
            header = request.getRemoteAddr();
        }
        list.add(header);
        header = request.getHeader(Constants.USER_AGENT);
        if(header != null){
            list.add(header);
        }
        header = request.getHeader(Constants.APP_NAME);
        if(header != null){
            list.add(header);
        }
        return list;
    }

    public boolean hasRoleAdmin(Set<String> roles){
        return roles.contains(Constants.ADMIN);
    }

    public String extractUserId(String path){
        String[] p = path.split(Constants.SLASH);
        if(p.length < 5){
            return "";
        }
        return path.split(Constants.SLASH)[4];
    }

    public Collection<SimpleGrantedAuthority> toAuthorities(Set<String> roles){
        if(roles == null || roles.isEmpty()){
            return List.of();
        }
        return roles
            .stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
