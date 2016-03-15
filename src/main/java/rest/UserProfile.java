package rest;

import org.jetbrains.annotations.NotNull;



public class UserProfile {
    private long id;
    @NotNull
    private String login;
    @NotNull
    private String password;

    private String email;

    public UserProfile(@NotNull String login, @NotNull String password, String email) {
        this.id = 0;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserProfile another = (UserProfile) o;

        return login.equals(another.login);

    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
