package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    {
        MealsUtil.meals.forEach(m -> save(m, m.getUserId()));
        log.info("repository: {}", repository);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        repository.putIfAbsent(userId, new ConcurrentHashMap<>());
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
            log.info("Saved {}", meal);
            return meal;
        }
        if (userMeals.get(meal.getId()).getUserId() != userId) {
            return null;
        }
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        Meal meal = userMeals.getOrDefault(id, null);
        if (meal == null || userMeals.get(id).getUserId() != userId) {
            return false;
        }
        return userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        Meal meal = userMeals.getOrDefault(id, null);
        if (meal == null || meal.getUserId() != userId) {
            log.info("No meal {} w id {} or wrong userId {}", meal, id, userId);
            return null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllFilteredByTime(int userId, LocalDate start, LocalDate end) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals.values().stream()
                .filter(m -> !m.getDate().isBefore(start) && !m.getDate().isAfter(end))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

