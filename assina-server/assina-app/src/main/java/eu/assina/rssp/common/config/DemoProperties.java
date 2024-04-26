package eu.assina.rssp.common.config;

import eu.assina.rssp.api.model.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
    private List<DemoUser> users = new ArrayList<>();

    public void setUsers(List<DemoUser> users) {
        this.users = users;
    }

    public List<DemoUser> getUsers() {
        return users;
    }

    public static class DemoUser extends User {
        private String username;
        private String name;
        private String email;
        private String role;

        private int numCredentials;

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String getRole() {
            return role;
        }

        @Override
        public void setRole(String role) {
            this.role = role;
        }

        public int getNumCredentials() {
            return numCredentials;
        }
        public void setNumCredentials(int numCredentials) {
            this.numCredentials = numCredentials;
        }
    }
}
