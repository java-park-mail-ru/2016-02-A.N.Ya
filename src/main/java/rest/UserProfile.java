package rest;

import org.jetbrains.annotations.NotNull;



public class UserProfile {
    private long id;
    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private String email;

    public UserProfile() {
        id = 0;
        login = "";
        password = "";
        email = "";
    }

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public UserProfile(long id, @NotNull String login, @NotNull String password, @NotNull String email) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }



    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        UserProfile that = (UserProfile) other;

        return login.equals(that.login);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : " + id +
                ", \"login\" : \"" + password + '\"' +
                ", \"email\" : \"" + email + '\"' +
                "}";
    }
}
