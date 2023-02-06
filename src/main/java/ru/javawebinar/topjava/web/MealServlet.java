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
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final static String ALL_MEALS_LIST = "meals.jsp";

    private final static String EDIT_MEAL = "updateMeal.jsp";

    private final static String ADD_MEAL = "addMeal.jsp";

    private MealDao dao;

    @Override
    public void init() throws ServletException {
        dao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action") == null ? "showList" : request.getParameter("action");
        switch (action) {
            case "update": {
                log.info("update action call");
                int mealId = getId(request);
                Meal meal = dao.findById(mealId);
                request.setAttribute("meal", meal);
                forwardRequest(request, response, EDIT_MEAL);
                break;
            }
            case "add": {
                log.info("add action call");
                LocalDateTime currentDate = LocalDateTime.now();
                request.setAttribute("date", currentDate);
                forwardRequest(request, response, ADD_MEAL);
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
                log.info("default switch call. Action is " + action);
                request.setAttribute("meals", MealsUtil.filteredByStreams(dao.getAll(), MealsUtil.CALORIES_PER_DAY));
                forwardRequest(request, response, ALL_MEALS_LIST);
                break;
            }
        }
    }

    private static int getId(HttpServletRequest request) {
        int mealId = Integer.parseInt(request.getParameter("id"));
        return mealId;
    }

    private static void forwardRequest(HttpServletRequest request, HttpServletResponse response, String forward) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String mealId = req.getParameter("mealId");
        log.debug(mealId);
        if (mealId == null || mealId.isEmpty()) {
            Meal meal = new Meal(dateTime, description, calories);
            dao.addNew(meal);
        } else {
            int id = Integer.parseInt(mealId);
            Meal meal = new Meal(dateTime, description, calories);
            meal.setId(id);
            dao.update(meal);
        }
        resp.sendRedirect("meals");
    }
}
