package io.github.alberes.register.manager.authorization.filters;

import io.github.alberes.register.manager.authorization.constants.Constants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class HeaderExposureFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.addHeader(Constants.ACCESS_CONTROL_EXPOSE_HEADERS, Constants.LOCATION);
        chain.doFilter(request, response);
    }
}