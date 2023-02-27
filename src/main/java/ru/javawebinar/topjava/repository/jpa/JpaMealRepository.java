package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    EntityManager manager;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        User u = manager.getReference(User.class, userId);
        meal.setUser(u);
        if (meal.isNew()) {
            manager.persist(meal);
            return meal;
        } else {
            Meal m = manager.getReference(Meal.class, meal.getId());
            if (m.getUser().getId() != userId) {
                return null;
            }
            return manager.merge(meal);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        return manager.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return DataAccessUtils.singleResult(manager.createNamedQuery(Meal.GET, Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getResultList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return manager.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return manager.createNamedQuery(Meal.BETWEEN_HALF_OPEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("start", startDateTime)
                .setParameter("end", endDateTime)
                .getResultList();
    }
}