package com.eidtrust.signer.rssp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eidtrust.signer.rssp.api.model.AuthProvider;
import com.eidtrust.signer.rssp.api.model.RoleName;
import com.eidtrust.signer.rssp.api.model.User;
import com.eidtrust.signer.rssp.api.payload.ApiResponse;
import com.eidtrust.signer.rssp.api.payload.AuthResponse;
import com.eidtrust.signer.rssp.api.payload.LoginRequest;
import com.eidtrust.signer.rssp.api.payload.SignUpRequest;
import com.eidtrust.signer.rssp.common.error.ApiException;
import com.eidtrust.signer.rssp.common.error.AssinaError;
import com.eidtrust.signer.rssp.repository.UserRepository;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AssinaAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAuthenticationTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getName())) {
            throw new ApiException(AssinaError.UserEmailAlreadyUsed, "Username {} already in use.", signUpRequest.getName());
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ApiException(AssinaError.UserEmailAlreadyUsed, "Email address {} already in use.", signUpRequest.getEmail());
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEncodedPIN(passwordEncoder.encode(signUpRequest.getPin()));
        user.setRole(RoleName.ROLE_USER.name());

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

}
