package es.upm.miw.easygive_spring.dtos;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCredentialDto {

    @NotNull
    private String password;

    public UserCredentialDto() {
        // Empty for framework
    }

    public UserCredentialDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserCredentialDto{" +
                "password='" + password + '\'' +
                '}';
    }
}
