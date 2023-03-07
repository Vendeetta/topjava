package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository mealCrudRepository;
    private final CrudUserRepository userCrudRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userCrudRepository) {
        this.mealCrudRepository = crudRepository;
        this.userCrudRepository = userCrudRepository;
    }

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userCrudRepository.getReferenceById(userId));
        if (meal.isNew()) {
            return mealCrudRepository.save(meal);
        }
        Meal m = mealCrudRepository.getByIdAndUserId(meal.id(), userId);
        if (m == null) {
            return null;
        }
        return mealCrudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealCrudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealCrudRepository.getByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealCrudRepository.getAllByUserId(userId, Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealCrudRepository.getBetweenHalfOpen(userId, startDateTime, endDateTime);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return mealCrudRepository.getMealByIdAndUserIdWithUser(id, userId);
    }
}
