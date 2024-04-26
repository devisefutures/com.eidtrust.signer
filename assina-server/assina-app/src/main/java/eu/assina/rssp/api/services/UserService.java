package eu.assina.rssp.api.services;

import eu.assina.rssp.api.model.User;
import eu.assina.rssp.api.payload.UserProfile;
import eu.assina.rssp.common.error.ApiException;
import eu.assina.rssp.common.error.AssinaError;
import eu.assina.rssp.repository.CredentialRepository;
import eu.assina.rssp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO rename to AssinaUserService
@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private CredentialRepository credentialRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       CredentialRepository credentialRepository) {
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isUsernameAvailable(String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return !isAvailable;
    }

    public boolean isEmailAvailable(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return isAvailable;
    }

    public User updateUser(User update) {
        User user = getUserById(update.getId()).orElseThrow(
                () -> new ApiException(AssinaError.UserNotFound, "Failed to find user {}", update.getId()));

        if (StringUtils.hasText(update.getPlainPassword())) {
            user.setPassword(passwordEncoder.encode(update.getPlainPassword()));
        }
        if (StringUtils.hasText(update.getPlainPIN())) {
            user.setEncodedPIN(passwordEncoder.encode(update.getPlainPIN()));
        }
        if (StringUtils.hasText(update.getEmail())) {
            user.setEmail(update.getEmail());
        }
        if (StringUtils.hasText(update.getName())) {
            // can update the name but not the username
            user.setName(update.getName());
        }
        if (StringUtils.hasText(update.getImageUrl())) {
            // can update the name but not the username
            user.setImageUrl(update.getImageUrl());
        }
        user.setUpdatedAt(Instant.now());
        final User updated = userRepository.save(user);
        return updated;
    }

    public Optional<UserProfile> getUserProfile(String id) {
        return getUserById(id).map(this::profile);
    }

    public List<UserProfile> getUserProfiles(Pageable pageable) {
        List<UserProfile> profiles = userRepository.findAll(pageable).stream().map(this::profile)
                                             .collect(Collectors.toList());

        return profiles;
    }

    public void deleteUser(String userId) {
        try {
            userRepository.deleteById(userId);
        }
        catch (EmptyResultDataAccessException ex) {
            throw new ApiException(AssinaError.UserNotFound, "Attempted to delete user that does exist", userId);
        }
    }

    /**
     * Functional to wrap user in profile
     */
    private UserProfile profile(User user) {
        return new UserProfile(user.getId(), user.getName(), user.getName(), user.getCreatedAt(),
                credentialRepository.countByOwner(user.getId()));
    }
}
