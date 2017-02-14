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
        Optional<String> t = userService.getAccessToken("user#2", "user#2");
        assertTrue(t.isPresent());
        assertEquals(t.get(), Role.REGULAR.toString());
        assertFalse(userService.isAllowed(t.get(), Action.DELETE_TEMPLATE));
    }

    @Test
    public void checkExistent() throws Exception {
        Optional<String> t = userService.getAccessToken("user#1", "user#1");
        assertTrue(t.isPresent());
        assertEquals(t.get(), Role.ADMINISTRATOR.toString());
        assertTrue(userService.isAllowed(t.get(), Action.DELETE_TEMPLATE));
    }
}