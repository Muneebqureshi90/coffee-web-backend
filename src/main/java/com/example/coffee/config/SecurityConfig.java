package com.example.coffee.config;




import com.example.coffee.security.CustomUserDetailsServiceImp;
import com.example.coffee.security.JwtAuthicationEntryPoint;
import com.example.coffee.security.JwtAuthicationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
//For swagger MVC
@EnableWebMvc

public class SecurityConfig  {
    @Autowired
    private CustomUserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    private JwtAuthicationEntryPoint point;
    @Autowired
    private JwtAuthicationFilter filter;


    // Configuration Method for Authentication
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsServiceImp).passwordEncoder(passwordEncoder());
    }
    // Create an AuthenticationManager bean


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeRequests()


                . requestMatchers("/test")
                .authenticated()
//                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**"
                        ,"/v2/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                        ,"/swagger-resources"
                        ,"/webjars/**").permitAll()
                .requestMatchers(HttpMethod.GET).permitAll()
//                .requestMatchers(HttpMethod.POST).permitAll()
//                .requestMatchers(HttpMethod.DELETE).permitAll()

                .anyRequest()
                .authenticated()
                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.addFilterBefore(this.filter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                });

        return http.build();
    }






    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

//    For react cross connection


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // Configure your CORS policy here
        // For example, to allow all origins, headers, and methods:
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://example.com", "http://another-example.com"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}