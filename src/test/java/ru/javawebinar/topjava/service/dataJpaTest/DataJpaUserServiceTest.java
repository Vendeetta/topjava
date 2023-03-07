package ru.javawebinar.topjava.service.dataJpaTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        User u = service.getWithMeals(UserTestData.USER_ID);
        USER_MATCHER.assertMatch(u, UserTestData.user);
        MEAL_MATCHER.assertMatch(u.getMeals(), MealTestData.meals);
    }

    @Test
    public void getUserWithEmptyMeals() {
        User u = service.getWithMeals(UserTestData.GUEST_ID);
        Assert.assertEquals(Collections.emptyList(), u.getMeals());
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }
}
