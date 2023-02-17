package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, breakfast);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, NOT_FOUND));
    }

    @Test
    public void getNotUserMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, NOT_FOUND));
    }

    @Test
    public void deleteNotUserMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate start = LocalDate.of(2023, 4, 15);
        LocalDate end = LocalDate.of(2023, 4, 16);
        List<Meal> resultList = service.getBetweenInclusive(start, end, USER_ID);
        assertMatch(resultList, lunch);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        List<Meal> testedMeals = Stream.of(breakfast, lunch, dinner).sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        assertMatch(meals, testedMeals);
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdated();
        service.update(updatedMeal, USER_ID);
        assertMatch(service.get(MEAL_ID, USER_ID), updatedMeal);
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(breakfast, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateCreate() {
        assertThrows(DataAccessException.class, () -> service.create(new Meal(LocalDateTime.of(2023, 4, 16, 11, 0, 0), "Duplicate", 100), USER_ID));
    }
}