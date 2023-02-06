package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal addNew(Meal meal);

    Meal update(Meal meal);

    void delete(int id);

    Meal findById(int id);

    List<Meal> getAll();
}
