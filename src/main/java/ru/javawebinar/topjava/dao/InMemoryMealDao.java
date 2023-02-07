package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements MealDao {
    private AtomicInteger count = new AtomicInteger(0);

    private Map<Integer, Meal> mealMemoryDataBase = new ConcurrentHashMap<>();

    public InMemoryMealDao() {
        Arrays.asList(
                        new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Завтрак", 500),
                        new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                        new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                        new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                        new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                        new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                        new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410))
                .forEach(this::addNew);
    }

    @Override
    public Meal addNew(Meal meal) {
        int id = count.incrementAndGet();
        meal.setId(id);
        mealMemoryDataBase.put(id, meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        if (!mealMemoryDataBase.containsKey(meal.getId())) {
            return null;
        }
        mealMemoryDataBase.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        mealMemoryDataBase.remove(id);
    }

    @Override
    public Meal findById(int id) {
        return mealMemoryDataBase.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMemoryDataBase.values());
    }
}
