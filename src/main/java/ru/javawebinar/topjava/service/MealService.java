package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private MealRepository repository;

    private final Logger log = LoggerFactory.getLogger(MealService.class);

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(int userId) {
        log.info("MealService#getAll called w userId={}", userId);
        return repository.getAll(userId);
    }

    public List<MealTo> getAllFilteredByTime(int userId, int caloriesPerDay, LocalDateTime start, LocalDateTime end) {
        return MealsUtil.getFilteredTos(repository.getAllFilteredByTime(userId, start.toLocalDate(), end.toLocalDate()), caloriesPerDay, start.toLocalTime(), end.toLocalTime());
    }

    public void update(Meal meal, int userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
}