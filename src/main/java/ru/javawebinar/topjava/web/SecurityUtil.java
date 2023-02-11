package ru.javawebinar.topjava.web;


import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static int userId;
    public static int authUserId() {
        return userId;
    }

    public static void setUserId(int authId){
        userId = authId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static boolean isMealBelongUser (int mealUserId, int userId){
        return userId == mealUserId;
    }
}