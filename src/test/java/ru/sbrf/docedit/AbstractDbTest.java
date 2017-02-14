package ru.sbrf.docedit;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(DbUnitTestExecutionListener.class)
@TestPropertySource(locations = "classpath:test.properties")
@ComponentScan(basePackageClasses = SimpleDocEditorApplication.class)
public class AbstractDbTest extends AbstractJUnit4SpringContextTests {
}
