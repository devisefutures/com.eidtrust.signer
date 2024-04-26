package eu.assina.rssp.api.payload;

import javax.validation.constraints.NotBlank;

/**
 * Holds a request object for local login
 */
public class LoginRequest {
    // Note: body is filled by Login.js in client app

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
