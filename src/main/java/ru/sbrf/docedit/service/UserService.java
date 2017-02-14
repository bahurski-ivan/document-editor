package ru.sbrf.docedit.service;

import ru.sbrf.docedit.model.user.Action;

import java.util.Optional;

/**
 * Interface for user service.
 */
public interface UserService {
    /**
     * Returns access token for user with given {@code userName} and {@code passwordMd5}.
     *
     * @return {@code Optional.empty()} if user not found
     */
    Optional<String> getAccessToken(String userName, String passwordMd5);

    /**
     * Checks if user with given {@code AccessToken} can preform {@code action}.
     *
     * @param accessToken user access token
     * @param action      action to perform
     * @return {@code true} if user allowed perform {@code action}
     */
    boolean isAllowed(String accessToken, Action action);
}
