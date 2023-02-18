package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ + 3;
    public static final Meal breakfast = new Meal(START_SEQ + 3, LocalDateTime.of(2023, 4, 12, 10, 0, 0), "Завтрак", 500);
    public static final Meal lunch = new Meal(START_SEQ + 4, LocalDateTime.of(2023, 4, 16, 11, 0, 0), "Обед", 500);
    public static final Meal dinner = new Meal(START_SEQ + 5, LocalDateTime.of(2023, 4, 12, 13, 0, 0), "Ужин", 999);
    public static final Meal adminBreakfast = new Meal(START_SEQ + 6, LocalDateTime.of(2023, 4, 12, 10, 0, 0), "ЗавтракАдмин", 750);
    public static final Meal adminLunch = new Meal(START_SEQ + 7, LocalDateTime.of(2023, 4, 12, 11, 0, 0), "ОбедАдмин", 250);
    public static final Meal adminDinner = new Meal(START_SEQ + 8, LocalDateTime.of(2023, 4, 12, 12, 0, 0), "УжинАдмин", 300);

    public static Meal getUpdated() {
        Meal meal = new Meal(breakfast);
        meal.setDescription("newDescription");
        meal.setCalories(987);
        meal.setDateTime(LocalDateTime.of(2020, 10, 12, 10, 11, 0));
        return meal;
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, 10, 12, 10, 0, 0), "newDescription", 100);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("userId").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("userId").isEqualTo(expected);
    }
}