package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String MEAL_REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService service;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(MEAL_REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEALTO_MATCHER.contentJson(MealsUtil.getTos(meals, CALORIES_PER_DAY)));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(MEAL_REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(MEAL_REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions actions = perform(MockMvcRequestBuilders.post(MEAL_REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andDo(print())
                .andExpect(status().isCreated());
        Meal created = MEAL_MATCHER.readFromJson(actions);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updateMeal = getUpdated();
        perform(put(MEAL_REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updateMeal)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), updateMeal);
    }

    @Test
    void getBetween() throws Exception {
        LocalDate startDate = LocalDate.of(2020, 1, 30);
        LocalDate endDate = LocalDate.of(2020, 1, 30);
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(20, 0);
        perform(MockMvcRequestBuilders.get(MEAL_REST_URL + "filter")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .param("startTime", startTime.toString())
                .param("endTime", endTime.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEALTO_MATCHER.contentJson(
                        MealsUtil.getFilteredTos(List.of(meal3, meal2, meal1), CALORIES_PER_DAY,
                                startTime, endTime)));
    }

    @Test
    void getBetweenWithoutTime() throws Exception {
        LocalDate startDate = LocalDate.of(2020, 1, 30);
        LocalDate endDate = LocalDate.of(2020, 1, 30);
        perform((MockMvcRequestBuilders.get(MEAL_REST_URL + "filter")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEALTO_MATCHER.contentJson(
                        MealsUtil.getFilteredTos(List.of(meal3, meal2, meal1), CALORIES_PER_DAY,
                                null, null)));
    }

    @Test
    void getBetweenWithoutDate() throws Exception {
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(20, 00);
        perform(MockMvcRequestBuilders.get(MEAL_REST_URL + "filter")
                .param("startTime", startTime.toString())
                .param("endTime", endTime.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEALTO_MATCHER.contentJson(
                        MealsUtil.getFilteredTos(meals, CALORIES_PER_DAY,
                                startTime, endTime)));
    }

    @Test
    void getBetweenWithEmptyAndNullParam() throws Exception {
        perform(MockMvcRequestBuilders.get(MEAL_REST_URL + "filter")
                .param("startDate", "")
                .param("endDate", "")
                .param("startTime", (String) null)
                .param("endTime", (String) null))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEALTO_MATCHER.contentJson(
                        MealsUtil.getTos(meals, CALORIES_PER_DAY)));
    }
}