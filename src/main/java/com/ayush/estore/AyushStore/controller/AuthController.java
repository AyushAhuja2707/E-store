package com.ayush.estore.AyushStore.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayush.estore.AyushStore.dtos.CartDto;
import com.ayush.estore.AyushStore.dtos.GoogleLoginRequest;
import com.ayush.estore.AyushStore.dtos.JWTRequest;
import com.ayush.estore.AyushStore.dtos.JWTResponse;
import com.ayush.estore.AyushStore.dtos.UserDtos;
import com.ayush.estore.AyushStore.entities.Providers;
import com.ayush.estore.AyushStore.entities.User;
import com.ayush.estore.AyushStore.exceptions.BadApiRequestException;
import com.ayush.estore.AyushStore.exceptions.ResourceNotFoundException;
import com.ayush.estore.AyushStore.security.JWTHelper;
import com.ayush.estore.AyushStore.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.google.client.id}")
    private String googleClientId;

    @Value("${app.google.default_password}")
    private String googleProviderDefaultPassword;

    // method to generate token
    @PostMapping("/generate-token")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {

        logger.info("Username {} Passsword {}", request.getEmail(), request.getPassword());
        this.doAuthenticate(request.getEmail(), request.getPassword());
        // generate token

        User userDetails = (User) userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtHelper.generateToken(userDetails);

        // send karna hai response
        JWTResponse jwtResponse = JWTResponse.builder().token(token)
                .userDtos(modelMapper.map(userDetails, UserDtos.class)).build();

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        // TODO Auto-generated method stub
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authentication);

        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid Username & Password");
        }
    }

    @PostMapping("/login-with-google")
    public ResponseEntity<JWTResponse> handleGoogleLogin(@RequestBody GoogleLoginRequest loginRequest)
            throws GeneralSecurityException, IOException
    // throws GeneralSecurityException, IOException
    {
        logger.info("Id  Token : {}", loginRequest.getIdToken());
        // return null;
        // token verify
        logger.info("Google Client Id {}", googleClientId);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new GsonFactory())
                .setAudience(List.of("570713149598-b5hubgna1ovp77s8vgvv1gl1i2rl9bno.apps.googleusercontent.com"))
                .build();
        logger.info("Reched");
        GoogleIdToken googleIdToken = verifier.verify(loginRequest.getIdToken());

        logger.info("Google Id token {}", googleIdToken);
        if (googleIdToken != null) {
            // token verified

            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            String email = payload.getEmail();
            String userName = payload.getSubject();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            logger.info("Name {}", name);
            logger.info("Email {}", email);
            logger.info("Picture {}", pictureUrl);
            logger.info("Username {}", userName);

            UserDtos userDto = new UserDtos();
            userDto.setName(name);
            userDto.setEmail(email);
            userDto.setImageName(pictureUrl);
            userDto.setPassword("570713149598");
            userDto.setAbout("user is created using google ");
            userDto.setProvider(Providers.GOOGLE);
            // logger.info("INNNNNNNNNN {}", userDto.getProvider());
            // //

            // UserDtos user = userService.createUser(userDto);
            // this.doAuthenticate(user.getEmail(), user.getPassword());

            // User user1 = modelMapper.map(user, User.class);

            // String token = jwtHelper.generateToken(user1);
            // // send karna hai response

            // JWTResponse jwtResponse =
            // JWTResponse.builder().token(token).userDtos(user).build();

            // JwtResponse.builder().token(token).user(user).build();

            // return ResponseEntity.ok(jwtResponse);

            UserDtos user = null;
            try {
                //
                logger.info("user is loaded from database");
                user = userService.getUserByEmail(userDto.getEmail());
                if (user.getProvider() == null) {
                    user.setProvider(Providers.SELF);
                }
                // // logic implement
                // // provider
                logger.info("AALAAA {}", user.getProvider());
                if (user.getProvider().equals(userDto.getProvider())) {
                    // continue
                } else {
                    throw new BadCredentialsException(
                            "Your email is already registered !! Try to login with username and password");
                }

            } catch (ResourceNotFoundException ex) {
                logger.info("This time user created: because this is new user ");
                user = userService.createUser(userDto);
            }

            // //
            this.doAuthenticate(user.getEmail(), userDto.getPassword());

            User user1 = modelMapper.map(user, User.class);

            String token = jwtHelper.generateToken(user1);
            // // send karna hai response

            JWTResponse jwtResponse = JWTResponse.builder().token(token).userDtos(user).build();

            return ResponseEntity.ok(jwtResponse);

        } else {
            logger.info("Token is invalid !!");
            throw new BadApiRequestException("Invalid Google User !!");
        }

    }

}
