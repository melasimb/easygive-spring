package es.upm.miw.easygive_spring.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;

@Document
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private LocalDateTime registrationDate;

    private String password;

    private String location;

    private String mobile;

    private String email;

    private Boolean active;

    private Role[] roles;

    public User() {
        this.registrationDate = LocalDateTime.now();
        this.active = true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && username.equals(((User) obj).username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", registrationDate=" + registrationDate +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }

    public static class Builder {
        private User user;

        private Builder() {
            this.user = new User();
            this.user.roles = new Role[]{Role.USER};
        }

        public Builder username(String username) {
            this.user.username = username;
            return this;
        }

        public Builder registrationDate(LocalDateTime registrationDate) {
            this.user.registrationDate = registrationDate;
            return this;
        }

        public Builder password(String password) {
            this.user.password = new BCryptPasswordEncoder().encode(password);
            return this;
        }

        public Builder location(String location) {
            this.user.location = location;
            return this;
        }

        public Builder email(String email) {
            this.user.email = email;
            return this;
        }

        public Builder active(Boolean active) {
            this.user.active = active;
            return this;
        }

        public Builder roles(Role... roles) {
            this.user.roles = roles;
            return this;
        }

        public Builder mobile(String mobile) {
            this.user.mobile = mobile;
            return this;
        }

        public User build() {
            return this.user;
        }
    }
}