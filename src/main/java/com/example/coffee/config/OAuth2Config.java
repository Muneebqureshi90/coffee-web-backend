package com.example.coffee.config;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.Arrays;

@Configuration
public class OAuth2Config {
//        @Value("${spring.security.oauth2.client.registration.google.client-id}")
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    //    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
//    private String facebookClientId;
//
//    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
//    private String facebookClientSecret;
//
    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String githubClientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String githubClientSecret;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(Arrays.asList(
                getGoogleClientRegistration(),
//                getFacebookClientRegistration(),
                githubClientRegistration()
        ));
    }

    private ClientRegistration getGoogleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v3/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .clientName("Google")
                .build();
    }

    //    private ClientRegistration getFacebookClientRegistration() {
//        return ClientRegistration.withRegistrationId("facebook")
//                .clientId(facebookClientId)
//                .clientSecret(facebookClientSecret)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
////                .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
//                .scope("email")
//                .authorizationUri("https://www.facebook.com/v12.0/dialog/oauth")
//                .tokenUri("https://graph.facebook.com/v12.0/oauth/access_token")
//                .userInfoUri("https://graph.facebook.com/me?fields=id,name,email")
//                .userNameAttributeName("id")
//                .clientName("Facebook")
//                .build();
//    }
    private ClientRegistration githubClientRegistration() {
        return ClientRegistration
                .withRegistrationId("github")
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                .redirectUri("http://localhost:8080/login/oauth2/code/github")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("id")
                .clientName("GitHub")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
    }
}