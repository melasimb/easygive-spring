package es.upm.miw.easygive_spring;

import es.upm.miw.easygive_spring.business_services.JwtService;
import es.upm.miw.easygive_spring.documents.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (jwtService.isBearer(authHeader)) {
            List<GrantedAuthority> authorities;
            authorities = jwtService.roles(authHeader).stream()
                    .map(role -> new SimpleGrantedAuthority(Role.valueOf(role).roleName())).collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(jwtService.user(authHeader), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}
