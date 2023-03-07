package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    Meal getByIdAndUserId(@Param("id") int id, @Param("userId") int userId);

    @Transactional()
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id and m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId")
    List<Meal> getAllByUserId(@Param("userId") int userId, Sort sort);

    @Query("SELECT m from Meal m WHERE m.user.id=:userId AND m.dateTime >= :start AND m.dateTime < :end ORDER BY m.dateTime DESC")
    List<Meal> getBetweenHalfOpen(@Param("userId") int userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId")
    Meal getMealByIdAndUserIdWithUser(@Param("id") int id, @Param("userId") int userId);
}
