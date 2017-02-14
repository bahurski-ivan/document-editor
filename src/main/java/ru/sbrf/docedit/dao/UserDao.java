package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.user.Role;
import ru.sbrf.docedit.model.user.User;

import java.util.Optional;

/**
 * Interface for {@code User} data access object.
 */
public interface UserDao {
    /**
     * Creates and returns new user.
     *
     * @param userName    user name
     * @param passwordMd5 password md5
     * @param role        role for new user
     * @return {@code Optional.empty()} if user with given {@code userName} already exist
     */
    Optional<User> createUser(String userName, String passwordMd5, Role role);

    /**
     * Retrieves user by userName + passwordMd5.
     *
     * @param userName    required user name
     * @param passwordMd5 password md5
     * @return {@code Optional.empty()} if no user found with given name + passwordMd5
     */
    Optional<User> getUser(String userName, String passwordMd5);
}
