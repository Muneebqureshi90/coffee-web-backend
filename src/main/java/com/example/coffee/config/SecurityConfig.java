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
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
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
                .requestMatchers("/test").authenticated()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/v2/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources", "/webjars/**").permitAll()
                .requestMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated()
                .and()
//                .oauth2Login(oauth2Login ->
//                        oauth2Login
////                                .loginPage("/login")
//
//                                .defaultSuccessUrl("/auth/login",true) // Redirect to a specific React page on success
////                                .failureUrl("/")
//                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

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



//    FOR GOOGGLE AND GITHUB AUTH
//@Bean
//public ClientRegistrationRepository clientRegistrationRepository() {
//    return new InMemoryClientRegistrationRepository(
//            googleClientRegistration(),
//            githubClientRegistration()
//            // Add more client registrations if needed
//    );
//}
//
//    private ClientRegistration googleClientRegistration() {
//        return ClientRegistration
//                .withRegistrationId("google")
//                .clientId("${spring.security.oauth2.client.registration.google.clientId}")
//                .clientSecret("${spring.security.oauth2.client.registration.google.clientSecret}")
//                .redirectUri("http://localhost:8080/login/oauth2/code/google") // Adjust the port and path
//                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
//                .tokenUri("https://accounts.google.com/o/oauth2/token")
//                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//                .userNameAttributeName("id")
//                .clientName("Google")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .build();
//    }
//
//    private ClientRegistration githubClientRegistration() {
//        return ClientRegistration
//                .withRegistrationId("github")
//                .clientId("${spring.security.oauth2.client.registration.github.clientId}")
//                .clientSecret("${spring.security.oauth2.client.registration.github.clientSecret}")
//                .redirectUri("http://localhost:8080/login/oauth2/code/github") // Adjust the port and path
//                .authorizationUri("https://github.com/login/oauth/authorize")
//                .tokenUri("https://github.com/login/oauth/access_token")
//                .userInfoUri("https://api.github.com/user")
//                .userNameAttributeName("id")
//                .clientName("GitHub")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .build();
//    }



}