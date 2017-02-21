package ru.sbrf.docedit.service.impl;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.user.Action;
import ru.sbrf.docedit.model.user.Role;
import ru.sbrf.docedit.service.UserService;

import java.util.Optional;

import static org.junit.Assert.*;
import static ru.sbrf.docedit.service.impl.DataSet.ADMIN_USER;
import static ru.sbrf.docedit.service.impl.DataSet.REGULAR_USER;

@DatabaseSetup("classpath:dataset/ServicesDataSet.xml")
@DatabaseTearDown("classpath:dataset/ServicesDataSet.xml")
public class SimpleRoleAccessTokenServiceTest extends AbstractDbTest {
    @Autowired
    private UserService userService;

    @Test
    public void checkAbsentUser() throws Exception {
        Optional<String> t = userService.getAccessToken("user#77777777777777", "");
        assertFalse(t.isPresent());
    }

    @Test
    public void accessCheck() throws Exception {
        String token = userService.getAccessToken(REGULAR_USER.getLogin(), REGULAR_USER.getPasswordMd5()).orElse(null);
        assertEquals(token, Role.REGULAR.toString());
        assertFalse(userService.isAllowed(token, Action.DELETE_TEMPLATE));
    }

    @Test
    public void checkExistent() throws Exception {
        String token = userService.getAccessToken(ADMIN_USER.getLogin(), ADMIN_USER.getPasswordMd5()).orElse(null);
        assertEquals(token, Role.ADMINISTRATOR.toString());
        assertTrue(userService.isAllowed(token, Action.DELETE_TEMPLATE));
    }
}