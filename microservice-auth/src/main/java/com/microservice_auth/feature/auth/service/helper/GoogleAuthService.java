package com.microservice_auth.feature.auth.service.helper;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.microservice_auth.feature.user.entity.User;
import com.microservice_auth.feature.user.enums.AccountStatus;
import com.microservice_auth.common.exception.UnauthorizedAccessException;
import com.microservice_auth.feature.user.enums.AuthProvider;
import com.microservice_auth.feature.user.service.IUserService;
import com.microservice_auth.feature.user.repository.IUserRepository;
import com.microservice_auth.feature.user.service.helper.UserAvatarService;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.microservice_auth.common.constants.ErrorMessageConstants.*;

@RequiredArgsConstructor
@Service
public class GoogleAuthService {


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();

    private GoogleIdTokenVerifier verifier;

    private final IUserRepository userRepository;

    private final IUserService userService;

    private final UserAvatarService userAvatarService;

    @PostConstruct
    public void initVerifier() throws Exception {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        this.verifier = new GoogleIdTokenVerifier.Builder(transport, GSON_FACTORY)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .setIssuer("https://accounts.google.com")
                .build();
    }


    public Long authenticateWithGoogle(String idToken) throws Exception {
        GoogleIdToken.Payload payload = validateGoogleToken(idToken);

        User user = handleSaveGoogleUser(payload);
        return user.getId();
    }

    public GoogleIdToken.Payload validateGoogleToken(String idTokenString) throws Exception {
        GoogleIdToken idToken = verifier.verify(idTokenString);


        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            if (payload.getExpirationTimeSeconds() * 1000 < System.currentTimeMillis()){
                throw new JwtException(TOKEN_EXPIRED);
            }

            return payload;
        }

        throw new JwtException(INVALID_TOKEN);

    }

    private User handleSaveGoogleUser(GoogleIdToken.Payload payload) {
        Optional<User> userOptional = userRepository.findByEmail(payload.getEmail());

        if (userOptional.isPresent()) {
            handleAccountStatus(userOptional.get());
            return userOptional.get();
        }

        User newUser = User.builder()
                .email(payload.getEmail())
                .fullName((String) payload.get("name"))
                .avatarUrl(null)
                .accountStatus(AccountStatus.ACTIVE)
                .role(userService.getDefaultUserRole())
                .provider(AuthProvider.GOOGLE)
                .build();

        User savedUser = userRepository.save(newUser);

        String googleAvatarUrl = (String) payload.get("picture");

        String uploadedAvatarUrl = userAvatarService.uploadAvatarFromUrl(googleAvatarUrl, savedUser.getId());


        savedUser.setAvatarUrl(uploadedAvatarUrl);
        return userRepository.save(savedUser);
    }


    private void handleAccountStatus(User user) {
        switch (user.getAccountStatus()) {
            case PENDING_VERIFICATION -> {
                user.setAccountStatus(AccountStatus.ACTIVE);
                userRepository.save(user);
            }
            case SUSPENDED -> throw new UnauthorizedAccessException(ACCOUNT_SUSPENDED);
            case BANNED -> throw new UnauthorizedAccessException(ACCOUNT_BANNED);
            case ACTIVE -> {}
            default -> throw new UnauthorizedAccessException(INVALID_ACCOUNT_STATUS);
        }
    }

}
