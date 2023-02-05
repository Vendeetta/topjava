package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {
    void addNewMeal(Meal meal);

    void updateMeal(Integer id, LocalDateTime dateTime, String description, int calories);

    void deleteMeal(int id);

    Meal findMealById(int id);

    List<Meal> readAll();

}
