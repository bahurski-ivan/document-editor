package ru.sbrf.docedit.model.user;

/**
 * Holder class for user credentials and his/her role.
 */
public class User {
    private final long userId;
    private final String login;
    private final String passwordMd5;
    private final Role role;

    public User(long userId, String login, String passwordMd5, Role role) {
        this.userId = userId;
        this.login = login;
        this.passwordMd5 = passwordMd5;
        this.role = role;
    }

    public long getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", login='" + login + '\'' +
                ", passwordMd5='" + passwordMd5 + '\'' +
                ", role=" + role +
                '}';
    }
}
