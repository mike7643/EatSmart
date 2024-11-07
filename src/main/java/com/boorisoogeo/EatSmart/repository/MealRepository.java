package com.boorisoogeo.EatSmart.repository;

import com.boorisoogeo.EatSmart.domain.Diet;
import com.boorisoogeo.EatSmart.domain.Food;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MealRepository {

    private final NamedParameterJdbcTemplate template;

    public MealRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Diet> getDietByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM diet WHERE user_id = :userId AND date_time BETWEEN :startDate AND :endDate";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("startDate", startDate.atStartOfDay());
        params.addValue("endDate", endDate.atTime(23, 59, 59));

        List<Diet> diets = template.query(sql, params, (rs, rowNum) -> {
            Diet diet = new Diet();
            diet.setId(rs.getLong("id"));
            diet.setUserId(rs.getLong("user_id"));
            diet.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
            return diet;
        });

        for (Diet diet : diets) {
            diet.setBreakfast(getFoodsByMealType(diet.getId(), "breakfast"));
            diet.setLunch(getFoodsByMealType(diet.getId(), "lunch"));
            diet.setDinner(getFoodsByMealType(diet.getId(), "dinner"));
        }

        return diets;
    }


    public Diet getDietByUserIdAndDate(Long userId, LocalDate date) {
        String sql = "SELECT * FROM diet WHERE user_id = :userId AND FORMATDATETIME(date_time, 'yyyy-MM-dd') = :date";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("date", date.toString());

        List<Diet> diets = template.query(sql, params, (rs, rowNum) -> {
            Diet diet = new Diet();
            diet.setId(rs.getLong("id"));
            diet.setUserId(rs.getLong("user_id"));
            diet.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
            return diet;
        });

        if (diets.isEmpty()) {
            return createEmptyDiet(userId, date);
        }

        Diet diet = diets.get(0);
        diet.setBreakfast(getFoodsByMealType(diet.getId(), "breakfast"));
        diet.setLunch(getFoodsByMealType(diet.getId(), "lunch"));
        diet.setDinner(getFoodsByMealType(diet.getId(), "dinner"));

        return diet;
    }

    public Diet getDietByUserId(Long userId) {
        String sql = "SELECT * FROM diet WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);

        List<Diet> diets = template.query(sql, params, (rs, rowNum) -> {
            Diet diet = new Diet();
            diet.setId(rs.getLong("id"));
            diet.setUserId(rs.getLong("user_id"));
            return diet;
        });

        if (diets.isEmpty()) {
            return createEmptyDiet(userId, LocalDate.now());
        }

        Diet diet = diets.get(0);
        diet.setBreakfast(getFoodsByMealType(diet.getId(), "breakfast"));
        diet.setLunch(getFoodsByMealType(diet.getId(), "lunch"));
        diet.setDinner(getFoodsByMealType(diet.getId(), "dinner"));

        return diet;
    }

    private Diet createEmptyDiet(Long userId, LocalDate date) {
        Diet newDiet = new Diet();
        newDiet.setUserId(userId);
        newDiet.setDateTime(date.atStartOfDay());
        newDiet.setBreakfast(new ArrayList<>());
        newDiet.setLunch(new ArrayList<>());
        newDiet.setDinner(new ArrayList<>());
        saveNewDiet(newDiet);
        return newDiet;
    }

    private void saveNewDiet(Diet diet) {
        String sql = "INSERT INTO diet (user_id, date_time) VALUES (:userId, :dateTime)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", diet.getUserId());
        params.addValue("dateTime", diet.getDateTime());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, params, keyHolder, new String[]{"id"});
        diet.setId(keyHolder.getKey().longValue());
    }

    public void saveDiet(Diet diet) {
        // 기존 음식을 삭제합니다.
        String deleteSql = "DELETE FROM diet_food WHERE diet_id = :dietId";
        MapSqlParameterSource deleteParams = new MapSqlParameterSource("dietId", diet.getId());
        template.update(deleteSql, deleteParams);

        // 새로운 음식을 추가합니다.
        String insertSql = "INSERT INTO diet_food (diet_id, food_id, meal_type) VALUES (:dietId, :foodId, :mealType)";
        for (Food food : diet.getBreakfast()) {
            MapSqlParameterSource insertParams = new MapSqlParameterSource();
            insertParams.addValue("dietId", diet.getId());
            insertParams.addValue("foodId", food.getFoodId());
            insertParams.addValue("mealType", "breakfast");
            template.update(insertSql, insertParams);
        }
        for (Food food : diet.getLunch()) {
            MapSqlParameterSource insertParams = new MapSqlParameterSource();
            insertParams.addValue("dietId", diet.getId());
            insertParams.addValue("foodId", food.getFoodId());
            insertParams.addValue("mealType", "lunch");
            template.update(insertSql, insertParams);
        }
        for (Food food : diet.getDinner()) {
            MapSqlParameterSource insertParams = new MapSqlParameterSource();
            insertParams.addValue("dietId", diet.getId());
            insertParams.addValue("foodId", food.getFoodId());
            insertParams.addValue("mealType", "dinner");
            template.update(insertSql, insertParams);
        }
    }

    public void deleteFoodFromDiet(Long dietId, Long foodId, String mealType) {
        String sql = "DELETE FROM diet_food WHERE diet_id = :dietId AND food_id = :foodId AND meal_type = :mealType";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("dietId", dietId);
        params.addValue("foodId", foodId);
        params.addValue("mealType", mealType);
        template.update(sql, params);
    }

    private List<Food> getFoodsByMealType(Long dietId, String mealType) {
        String sql = "SELECT f.* FROM food f INNER JOIN diet_food df ON f.food_id = df.food_id WHERE df.diet_id = :dietId AND df.meal_type = :mealType";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("dietId", dietId);
        params.addValue("mealType", mealType);
        return template.query(sql, params, (rs, rowNum) -> {
            Food food = new Food();
            food.setFoodId(rs.getLong("food_id"));
            food.setFoodName(rs.getString("food_name"));
            food.setFoodKcal(rs.getDouble("food_kcal"));
            food.setFoodProtein(rs.getDouble("food_protein"));
            food.setFoodFat(rs.getDouble("food_fat"));
            food.setFoodCarbo(rs.getDouble("food_carbo"));
            food.setFoodSugar(rs.getDouble("food_sugar"));
            food.setFoodNat(rs.getDouble("food_nat"));
            food.setFoodColest(rs.getDouble("food_colest"));
            return food;
        });
    }
}
