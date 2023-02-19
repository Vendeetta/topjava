package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, UserTestData.USER_ID);
        assertMatch(meal, breakfast);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, UserTestData.USER_ID));
    }

    @Test
    public void getNotUserMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, UserTestData.ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, UserTestData.USER_ID));
    }

    @Test
    public void deleteNotUserMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, UserTestData.GUEST_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate start = LocalDate.of(2023, 4, 13);
        LocalDate end = LocalDate.of(2023, 4, 14);
        List<Meal> resultList = service.getBetweenInclusive(start, end, UserTestData.USER_ID);
        assertMatch(resultList, testMeal);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(UserTestData.USER_ID);
        assertMatch(meals, testMeal, dinner, lunch, breakfast);
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdated();
        service.update(updatedMeal, UserTestData.USER_ID);
        assertMatch(service.get(MEAL_ID, UserTestData.USER_ID), updatedMeal);
    }

    @Test
    public void updateNotFound() {
        Meal nonExistentMeal = new Meal(NOT_FOUND, LocalDateTime.now(), "test", 100);
        assertThrows(NotFoundException.class, () -> service.update(nonExistentMeal, UserTestData.USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), UserTestData.USER_ID);
        int newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, UserTestData.USER_ID), newMeal);
    }

    @Test
    public void duplicateDateCreate() {
        LocalDateTime testDateTime = breakfast.getDateTime();
        assertThrows(DataAccessException.class, () -> service.create(new Meal(testDateTime,
                "Duplicate", 100), UserTestData.USER_ID));
    }
}