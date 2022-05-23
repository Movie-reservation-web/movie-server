package study.movie.domain.member.entity;

public enum Role {

    MASTER, ADMIN, USER, GUEST;

    public String getGrantedAuthority() {
        return "ROLE_" + name();
    }
}
