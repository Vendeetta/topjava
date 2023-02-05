package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);

    private MealDao dao;

    @Override
    public void init() throws ServletException {
        dao = new MealDaoInMemoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");
        String forward = "";
        if (action == null) {
            //      final List<MealTo> allMeals = MealsUtil.filteredByStreams(MealsUtil.MEALS, MealsUtil.CALORIES_PER_DAY);
            request.setAttribute("meals", MealsUtil.filteredByStreams(dao.readAll(), MealsUtil.CALORIES_PER_DAY));
            forward = "meals.jsp";
        } else if (action.equalsIgnoreCase("update")) {
            forward = "meal.jsp";
            int mealId = Integer.parseInt(request.getParameter("id"));
            Meal meal = dao.findMealById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("add")) {
            forward = "meal.jsp";
        } else if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("id"));
            dao.deleteMeal(mealId);
            forward = "/meals.jsp";
            request.setAttribute("meals", MealsUtil.filteredByStreams(dao.readAll(), MealsUtil.CALORIES_PER_DAY));
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
        response.sendRedirect("meals.jsp");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime").replace("T", " "), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String mealId = req.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(dateTime, description, calories);
            dao.addNewMeal(meal);
        } else {
            Integer id = Integer.parseInt(mealId);
            dao.updateMeal(id, dateTime, description, calories);
        }
        RequestDispatcher view = req.getRequestDispatcher("meals.jsp");
        req.setAttribute("meals", MealsUtil.filteredByStreams(dao.readAll(), MealsUtil.CALORIES_PER_DAY));
        view.forward(req, resp);
    }
}
