package ru.javawebinar.topjava.service.userTests;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(profiles = Profiles.JDBC)
public class JdbcUserServiceTest extends UserServiceTest {
}
