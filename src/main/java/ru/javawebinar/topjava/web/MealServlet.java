package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String ALL_MEALS_LIST = "meals.jsp";

    private static final String ADD_EDIT = "updateMeal.jsp";

    private MealDao dao;

    @Override
    public void init() throws ServletException {
        dao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = Optional.ofNullable(request.getParameter("action"))
                .orElse("showList");
        switch (action) {
            case "update": {
                log.info("update action call");
                int mealId = getId(request);
                Meal meal = dao.findById(mealId);
                request.setAttribute("title", "Edit meal");
                request.setAttribute("meal", meal);
                forwardRequest(request, response, ADD_EDIT);
                break;
            }
            case "add": {
                log.info("add action call");
                Meal meal = new Meal();
                request.setAttribute("title", "Add meal");
                request.setAttribute("meal", meal);
                forwardRequest(request, response, ADD_EDIT);
                break;
            }
            case "delete": {
                log.info("delete action call");
                int mealId = getId(request);
                dao.delete(mealId);
                response.sendRedirect("meals");
                break;
            }
            default: {
                log.info("default switch call. Action is {}", action);
                request.setAttribute("meals", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY));
                forwardRequest(request, response, ALL_MEALS_LIST);
                break;
            }
        }
    }

    private static int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }

    private static void forwardRequest(HttpServletRequest request, HttpServletResponse response, String forward) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String mealId = req.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(dateTime, description, calories);
            Meal addedMeal = dao.addNew(meal);
            log.debug("Added new meal {}", addedMeal);
        } else {
            Meal meal = new Meal(Integer.parseInt(mealId), dateTime, description, calories);
            Meal updatedMeal = dao.update(meal);
            log.debug("Updated meal {}", updatedMeal);
        }
        resp.sendRedirect("meals");
    }
}
