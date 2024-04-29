package com.eidtrust.signer.rssp.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eidtrust.signer.rssp.api.model.User;
import com.eidtrust.signer.rssp.api.payload.UserIdentityAvailability;
import com.eidtrust.signer.rssp.api.payload.UserProfile;
import com.eidtrust.signer.rssp.api.services.UserService;
import com.eidtrust.signer.rssp.common.error.ApiException;
import com.eidtrust.signer.rssp.common.error.AssinaError;
import com.eidtrust.signer.rssp.security.CurrentUser;
import com.eidtrust.signer.rssp.security.UserPrincipal;

import java.util.List;

@RestController
public class AssinaUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        User user = userService.getUserById(userId).orElseThrow(
                () -> new ApiException(AssinaError.UserNotFound, "Failed to find user {}", userId));
        return user;
    }

    @PutMapping("/users/me")
    public User updateCurrentUser(@CurrentUser UserPrincipal userPrincipal,
                                  @RequestBody User user) {
        String currentUserId = userPrincipal.getId();
        user.setId(currentUserId);
        final User updated = userService.updateUser(user);
        return updated;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        return new UserIdentityAvailability(userService.isUsernameAvailable(username));
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        return new UserIdentityAvailability(userService.isEmailAvailable(email));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserProfile getUserProfile(@PathVariable(value = "id") String userId) {
        UserProfile userProfile = userService.getUserProfile(userId).orElseThrow(
                () -> new ApiException(AssinaError.UserNotFound, "Failed to find user {}", userId));
        return userProfile;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserProfile> getUsersPaginated(Pageable pageable) {
        List<UserProfile> userProfiles = userService.getUserProfiles(pageable);
        return userProfiles;
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User updateUser(@RequestBody User user,
                           @PathVariable(value = "userId") String userId) {
        user.setId(userId);
        final User updated = userService.updateUser(user);
        return updated;
    }

    @DeleteMapping("users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") String userId)
    {
        userService.deleteUser(userId);
    }
}
