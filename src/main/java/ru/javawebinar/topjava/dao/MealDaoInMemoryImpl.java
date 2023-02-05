package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemoryImpl implements MealDao {

    private static AtomicInteger count = new AtomicInteger(0);


    private Map<Integer, Meal> mealMemoryDataBase = new ConcurrentHashMap<>();

    {

        Meal m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Завтрак", 500);
        addNewMeal(m);
        m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        addNewMeal(m);
        m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        addNewMeal(m);
        m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        addNewMeal(m);
        m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        addNewMeal(m);
        m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        addNewMeal(m);
        m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
        addNewMeal(m);
    }

    @Override
    public void addNewMeal(Meal meal) {
        Integer id = count.incrementAndGet();
        meal.setId(id);
        mealMemoryDataBase.put(id, meal);
    }

    @Override
    public void updateMeal(Integer id, LocalDateTime dateTime, String description, int calories) {
        Meal meal = new Meal(dateTime, description, calories);
        meal.setId(id);
        mealMemoryDataBase.put(id, meal);
    }

    @Override
    public void deleteMeal(int id) {
        mealMemoryDataBase.remove(id);
    }

    @Override
    public Meal findMealById(int id) {
        return mealMemoryDataBase.get(id);
    }

    @Override
    public List<Meal> readAll() {
        return new ArrayList<>(mealMemoryDataBase.values());
    }
}
