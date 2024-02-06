package com.example.coffee.controllers;

import com.example.coffee.entity.User;
import com.example.coffee.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SecurityRequirement(name = "scheme1")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/")
@Tag(name = "OAuthentication2 Controller", description = "This is OAuthentication2 Controller")
public class OAuth2Controller {

    private final OAuth2AuthorizedClientService clientService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public OAuth2Controller(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    private static final Logger log = LoggerFactory.getLogger(OAuth2Controller.class);

    @GetMapping("/login/oauth2/code/{provider}")
    public RedirectView loginSuccess(@PathVariable String provider, OAuth2AuthenticationToken authenticationToken) {

        // Log the details for debugging
//        log.debug("Received authenticationToken: {}", authenticationToken);


        // Check if the authenticationToken is null

            if (authenticationToken == null) {
            // Log the error details
            log.error("OAuth2 authenticationToken is null. Unable to proceed with login.");
//            log.debug("Access Token Value: {}", accessToken.getTokenValue());
        log.debug("Received authenticationToken: {}", authenticationToken);

            // Handle the case where authenticationToken is null
            return new RedirectView("http://localhost:3000/error");
        }

            // Rest of your code...



        OAuth2AccessToken accessToken = clientService
                .loadAuthorizedClient(
                        authenticationToken.getAuthorizedClientRegistrationId(),
                        authenticationToken.getName()
                )
                .getAccessToken();

// Log or print the access token details
//        log.debug("Access Token Value: {}", accessToken.getTokenValue());



        // Extract user details from the OAuth2 principal (OidcUser)
        OidcUser oidcUser = (OidcUser) authenticationToken.getPrincipal();
        String userEmail = oidcUser.getEmail(); // Assuming email is part of the user claims

        // Check if the user already exists in the database
        Optional<User> existingUser = userRepository.findByEmail(userEmail);
        if (!existingUser.isPresent()) {
            // If the user doesn't exist, create a new user and save it to the database
            User newUser = new User();
            newUser.setEmail(userEmail);
            newUser.setProvider(provider);

            try {
                // Save the new user to the database
                newUser = userRepository.save(newUser);
                // Handle successful save, e.g., log or perform additional actions
            } catch (Exception e) {
                // Handle the exception, e.g., log the error or return an appropriate response
                e.printStackTrace();
                return new RedirectView("http://localhost:3000/error");
            }

        }

        // Redirect to your React frontend login success page
        return new RedirectView("http://localhost:3000/");
    }
}
