package es.upm.miw.easygive_spring.documents;

public enum Role {
    ADMIN, USER, AUTHENTICATED;

    public String roleName() {
        return "ROLE_" + this.toString();
    }
}
