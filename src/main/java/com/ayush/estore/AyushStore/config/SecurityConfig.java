package com.ayush.estore.AyushStore.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ayush.estore.AyushStore.security.JWTAuthenticationEntryPoint;
import com.ayush.estore.AyushStore.security.JWTAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JWTAuthenticationFilter filter;

    @Autowired
    private JWTAuthenticationEntryPoint entryPoint;

    final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/webjars/**",
            "swagger-resources/**",
    };

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        // configurations

        // urls
        // public koun se protected
        // koun se urls admin, koun se normal user.

        // configuring urls
        // cors ko hame abhi ke lie disable kiy hai
        // isko ham log baad mein sikhenge
        System.out.println("AALA" + security.toString());
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        // csrf ko hame abhi ke lie disable kiy hai

        // security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
        // .configurationSource(new CorsConfigurationSource() {
        // @Override
        // public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        // CorsConfiguration corsConfiguration = new CorsConfiguration();

        // // origins
        // // methods
        // // corsConfiguration.addAllowedOrigin("http://localhost:4200");

        // corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200",
        // "http://localhost:4300", "http://localhost:4300"));
        // // not used in prod
        // // corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        // corsConfiguration.setAllowedMethods(List.of("*"));
        // corsConfiguration.setAllowCredentials(true);
        // corsConfiguration.setAllowedHeaders(List.of("*"));
        // corsConfiguration.setMaxAge(4000L);

        // return corsConfiguration;
        // }
        // }));

        // // isko ham log baad mein sikhenge
        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        security.authorizeHttpRequests(request -> request

                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**")
                .permitAll()
                .requestMatchers(HttpMethod.DELETE, "/users/**")

                .hasRole(AppConstants.ROLE_ADMIN).requestMatchers(HttpMethod.PUT, "/users/**")
                .hasAnyRole(AppConstants.ROLE_ADMIN, AppConstants.ROLE_NORMAL)
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

                .requestMatchers("/products/**").hasRole(AppConstants.ROLE_ADMIN)
                .requestMatchers(HttpMethod.GET, "/users/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                .requestMatchers("/categories/**").hasRole(AppConstants.ROLE_ADMIN)
                .requestMatchers(HttpMethod.POST, "/auth/generate-token",
                        "/auth/login-with-google")
                .permitAll()

                // ,
                // "/auth/login-with-google",
                // "/auth/regenerate-token")
                // .permitAll()
                // .requestMatchers("/auth/**").authenticated()
                .anyRequest().permitAll()

        );

        // kis type ki security:
        // security.httpBasic(Customizer.withDefaults());

        // entry point
        security.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint));

        // session creation policy
        security.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // main -->
        security.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return security.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        System.out.println("In Auth Manager");
        return builder.getAuthenticationManager();
    }

}
