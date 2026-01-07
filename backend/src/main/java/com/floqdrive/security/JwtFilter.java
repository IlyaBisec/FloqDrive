package com.floqdrive.security;

import com.floqdrive.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter
{
    private final JwtProvider jwtProvider;
    // Service which load user by ID
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        System.out.println("JWT FILTER CHECK: " + request.getServletPath());
        String path = request.getServletPath();

        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/error")
                || path.startsWith("/api/auth");
    }

    // Check every HTTP request
    // - Take token from Authorization
    // - Validate JWT
    // - Put user to SecurityContext
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        // Take Authorization header
        String header = request.getHeader("Authorization");

        try {
            // Checking the Bearer token format
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                if (jwtProvider.validateToken(token)) {
                    // Retrieving userId from JWT
                    Long userId = jwtProvider.getUserIdFromToken(token);

                    // Loading the user
                    var userDetails = userDetailsService.loadUserById(userId);

                    // Create an authentication object
                    var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Add the user to the SecurityContext
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        catch (Exception exception)
        {
            SecurityContextHolder.clearContext();
            System.out.println("JWT authentication failed: "+exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}

