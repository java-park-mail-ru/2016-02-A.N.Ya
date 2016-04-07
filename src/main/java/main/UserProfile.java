package main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.json.Json;
import javax.persistence.*;

@Entity
@Table(name = "User")
public class UserProfile {
    @SuppressWarnings("InstanceVariableNamingConvention")
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "login", unique = true)
    private String login;


    @Column(name = "password")
    private String password;


    @Column(name = "email")
    private String email;


    public UserProfile() {

    }

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email) {
        this.id = 0;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email, long id) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = id;
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
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserProfile another = (UserProfile) o;

        return login.equals(another.login);

    }

    @Override
    public String toString() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("login", this.login)
                .add("email", this.email)
                .build()
                .toString();
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
