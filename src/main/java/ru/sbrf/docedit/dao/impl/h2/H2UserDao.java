package ru.sbrf.docedit.dao.impl.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.dao.UserDao;
import ru.sbrf.docedit.model.user.Role;
import ru.sbrf.docedit.model.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 09.02.2017.
 */
@Component
public class H2UserDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Creates and returns new user.
     *
     * @param userName    user name
     * @param passwordMd5 password md5
     * @param role        role for new user
     * @return {@code Optional.empty()} if user with given {@code userName} already exist
     */
    @Override
    public Optional<User> createUser(String userName, String passwordMd5, Role role) {
        final String sql = "INSERT INTO _USERS (role, user_name, password_md5) VALUES (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, role.name());
            ps.setString(2, userName);
            ps.setString(3, passwordMd5);
            return ps;
        }, keyHolder);

        return result == 1 ? Optional.of(new User(keyHolder.getKey().longValue(), userName, passwordMd5, role)) : Optional.empty();
    }

    /**
     * Retrieves user by userName + passwordMd5.
     *
     * @param userName    required user name
     * @param passwordMd5 password md5
     * @return {@code Optional.empty()} if no user found with given name + passwordMd5
     */
    @Override
    public Optional<User> getUser(String userName, String passwordMd5) {
        final String sql = "SELECT user_id, role, user_name, password_md5 FROM _USERS WHERE user_name LIKE ? AND password_md5 LIKE ?";
        List<User> result = jdbcTemplate.query(sql, UserRowMapper.INSTANCE, userName, passwordMd5);
        assert result.size() <= 1;
        return result.size() == 0 ? Optional.empty() : Optional.of(result.get(0));
    }

    private static class UserRowMapper implements RowMapper<User> {

        static final UserRowMapper INSTANCE = new UserRowMapper();

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("user_id"),
                    rs.getString("user_name"),
                    rs.getString("password_md5"),
                    Role.valueOf(rs.getString("role"))
            );
        }
    }
}