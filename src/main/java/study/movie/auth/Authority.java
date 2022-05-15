package study.movie.auth;

public enum Authority {
    ADMIN, USER;

    public String getRoles(){
        return "ROLE_" + name();
    }
}
