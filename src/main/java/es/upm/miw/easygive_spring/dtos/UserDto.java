package es.upm.miw.easygive_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import es.upm.miw.easygive_spring.documents.User;

import javax.validation.constraints.NotNull;

@JsonInclude(Include.NON_NULL)
public class UserDto {

    private String id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String location;

    @NotNull
    private String mobile;

    private String email;

    public UserDto() {
        // Empty for framework
    }

    public UserDto(String username, String password, String location, String mobile, String email) {
        this.username = username;
        this.password = password;
        this.location = location;
        this.mobile = mobile;
        this.email = email;
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.location = user.getLocation();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}