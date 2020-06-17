package es.upm.miw.easygive_spring.dtos;

public class TokenOutputDto {

    private String token;

    public TokenOutputDto() {
        // Empty for framework
    }

    public TokenOutputDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenOutputDto{" +
                "token='" + token + '\'' +
                '}';
    }
}