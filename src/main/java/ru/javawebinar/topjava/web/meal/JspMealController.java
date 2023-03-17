package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    MealService service;

    @Autowired
    public JspMealController(MealService service) {
        this.service = service;
    }

    @GetMapping
    public String getAll(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        String act = request.getParameter("action");
        if (act == null) {
            model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()),
                    SecurityUtil.authUserCaloriesPerDay()));
            return "meals";
        }
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", MealsUtil.getFilteredTos(service.getBetweenInclusive(startDate,
                endDate, userId), SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
        log.info("/meals getAll");
        return "/meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        int id = getId(request);
        service.delete(id, userId);
        log.info("delete meal w id = {}, for user {}", id, userId);
        return "redirect:/meals";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = service.get(getId(request), userId);
        request.setAttribute("meal", meal);
        request.setAttribute("isCreating", false);
        log.info("update meal: {}, for user {}", meal, userId);
        return "mealForm";
    }

    @GetMapping("/create")
    public String create(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        request.setAttribute("meal", meal);
        request.setAttribute("isCreating", true);
        log.info("create meal: {} for user {}", meal, userId);
        return "mealForm";
    }

    @PostMapping("/new")
    public String saveNew(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        checkNew(meal);
        log.info("save new {} for user {}", meal, userId);
        service.create(meal, userId);
        return "redirect:/meals";
    }

    @PostMapping("/edit")
    public String edit(HttpServletRequest request) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        assureIdConsistent(meal, Integer.parseInt(request.getParameter("id")));
        log.info("update {} for user {}", meal, userId);
        service.update(meal, userId);
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
