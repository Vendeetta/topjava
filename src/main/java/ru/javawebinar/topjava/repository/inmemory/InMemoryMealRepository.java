package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    {
        MealsUtil.meals.forEach(this::initSave);
        log.info("repository: {}", repository);
    }

    //мето для заполнения мапы.
    public void initSave(Meal meal) {
        counter.incrementAndGet();
        repository.put(meal.getId(), meal);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        if (!SecurityUtil.isMealBelongUser(repository.get(meal.getId()).getUserId(), meal.getUserId()))
            return null;
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (!SecurityUtil.isMealBelongUser(repository.get(id).getUserId(), userId))
            return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        if (!SecurityUtil.isMealBelongUser(repository.get(id).getUserId(), userId))
            return null;
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream().filter(m -> m.getUserId() == userId)
                .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                .collect(Collectors.toList());
    }
}

