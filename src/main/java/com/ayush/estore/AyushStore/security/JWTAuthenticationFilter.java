package com.ayush.estore.AyushStore.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // api se pehle chalega jwt ko verify karnekeliye
        // TODO Auto-generated method stub

        System.out.println("REQQQQQQQ-->>" + request.toString());
        // Authorization Bearer <token>
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header {}", requestHeader);

        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            logger.info("Token {}", token);
            try {
                username = jwtHelper.getUsernameFromToken(token);
                logger.info("Token Username {}", username);

            } catch (IllegalArgumentException ex) {
                logger.info("Illegal Arg while fetching the username {}", ex.getMessage());
            } catch (ExpiredJwtException ex) {
                logger.info("Token Expired {}", ex.getMessage());
            } catch (MalformedJwtException ex) {
                logger.info("Some changed has done in token || Invalid token {}", ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            logger.info("Invalid Header !! Header is not starting with Bearer");
        }
        // if username kuch hai to
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("User is authenticated");
            logger.info("Userr-----------------------" + username);
            // username mein kuch hai
            // authentication null
            // authentication ko set karega
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // validate token
            if (username.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)) {
                // token valid hai
                // security context ke andar authentcaion set karenge
                UsernamePasswordAuthenticationToken authentcaion = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());
                authentcaion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentcaion);
            }

        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Exclude Swagger-related paths
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs/") ||
                path.startsWith("/swagger-resources/") ||
                path.startsWith("/webjars/");
    }
}
