package com.historia.zetu.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.historia.zetu.services.serviceImp.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class Config {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/about-us",
                                "/about",
                                "/login",
                                "/contact-us",
                                "/blog",
                                "/api/subscription/**",
                                "/blog/images/**",
                                "/api/contactus",
                                "api/placeholder/**",
                                "/api/stories/**",
                                "/view-story/**",
                                "/api/histories/view-story/**",
                                "/api/histories/view-entire-story/**",
                                "/api/histories",
                                "/api/histories/**",
                                "/api/histories/like/**",
                                "/history/**",
                                "/authentication",
                                "/sign-in",
                                "/api/signup",
                                "/api/verify-otp",
                                "/forgot-password",
                                "/view-story/**"
                        ).permitAll()
                        .requestMatchers("/api/dashboard/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())

                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .failureHandler((request, response, exception) ->
                                response.sendRedirect("/authentication?error=" + exception.getMessage()))
                        .loginPage("/authentication").permitAll()
                )
                .anonymous(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(3)
                                .maxSessionsPreventsLogin(true)
                                .expiredUrl("/authentication?login_expired=true")
                        )
                )
                .rememberMe(remember -> remember
                        .key("uniqueAndSecret")
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(7 * 24 * 60 * 60)  // 7 days
                        .rememberMeCookieName("remember-me-cookie")
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/dashboard/img/**",
                "/dashboard/js/**",
                "/dashboard/plugins/**",
                "/dashboard/dist/**",
                "/dashboard/src/**",
                "/blog/js/**",
                "/blog/css/**",
                "/blog/images/**"
        );
    }

    @Bean
    public ServletContextInitializer cookieSameSiteInitializer() {
        return servletContext -> {
            servletContext.getSessionCookieConfig().setAttribute("SameSite", "None");
            servletContext.getSessionCookieConfig().setSecure(true);
        };
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: to format dates properly
        return objectMapper;
    }

}
