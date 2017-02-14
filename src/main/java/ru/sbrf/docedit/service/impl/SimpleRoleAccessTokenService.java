package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.dao.UserDao;
import ru.sbrf.docedit.model.user.Action;
import ru.sbrf.docedit.model.user.Role;
import ru.sbrf.docedit.model.user.User;
import ru.sbrf.docedit.service.UserService;

import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 10.02.2017.
 */
@Component
public class SimpleRoleAccessTokenService implements UserService {
    private final UserDao userDao;

    @Autowired
    public SimpleRoleAccessTokenService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<String> getAccessToken(String userName, String passwordMd5) {
        final Optional<User> u = userDao.getUser(userName, passwordMd5);
        if (!u.isPresent())
            return Optional.empty();
        return Optional.of(u.get().getRole().toString());
    }

    @Override
    public boolean isAllowed(String accessToken, Action action) {
        Role r;

        try {
            r = Role.valueOf(accessToken);
        } catch (IllegalArgumentException e) {
            r = Role.REGULAR;
        }

        return r.allowedToPerform(action);
    }
}
