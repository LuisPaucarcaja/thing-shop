package com.authcore.security;


import com.authcore.dto.ApiResponse;
import com.authcore.dto.AuthenticatedUser;
import com.authcore.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;


@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenValidator jwtTokenValidator;

    @Autowired
    public JwtFilter(JwtTokenValidator jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException {

        try {
            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                if (jwtTokenValidator.isTokenValid(token)) {
                    String userId = jwtTokenValidator.getUserId(token);
                    String role = jwtTokenValidator.getUserRole(token);

                    AuthenticatedUser principal = new AuthenticatedUser(Long.parseLong(userId));

                    String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    null,
                                    List.of(new SimpleGrantedAuthority(authority))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | UnauthorizedException ex) {
            writeErrorResponse(response, ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            writeErrorResponse(response, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void writeErrorResponse(HttpServletResponse response, String errorMessage, HttpStatus status)
            throws IOException {
        if (response.isCommitted()) return;

        response.resetBuffer();
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse apiResponse = ApiResponse.error(errorMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }



}