package com.boorisoogeo.EatSmart.repository;

import com.boorisoogeo.EatSmart.domain.Food;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FoodRepository {
    private final NamedParameterJdbcTemplate template;

    public FoodRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Food save(FoodUpdateDto foodUpdateDto) {
        Food food = new Food();

        food.setFoodKcal(foodUpdateDto.getFoodKcal());
        food.setFoodProtein(foodUpdateDto.getFoodProtein());
        food.setFoodFat(foodUpdateDto.getFoodFat());
        food.setFoodCarbo(foodUpdateDto.getFoodCarbo());
        food.setFoodSugar(foodUpdateDto.getFoodSugar());
        food.setFoodNat(foodUpdateDto.getFoodNat());
        food.setFoodColest(foodUpdateDto.getFoodColest());

        String sql = "insert into food (food_name, food_kcal, food_protein, food_fat, food_carbo, food_sugar, food_nat, food_colest) "+
                "values (:foodName, :foodKcal, :foodProtein, :foodFat, :foodCarbo, :foodSugar, :foodNat, :foodColest)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(food);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        food.setFoodId(key);
        return food;
    }

    public void update(Long foodId, FoodUpdateDto updateParam) {
        String sql = "update food set " +
                "food_name = :foodName, " +
                "food_kcal = :foodKcal, " +
                "food_protein = :foodProtein, " +
                "food_fat = :foodFat, " +
                "food_carbo = :foodCarbo, " +
                "food_sugar = :foodSugar, " +
                "food_nat = :foodNat, " +
                "food_colest = :foodColest " +
                "where food_id = :foodId";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("foodName", updateParam.getFoodName())
                .addValue("foodKcal", updateParam.getFoodKcal())
                .addValue("foodProtein", updateParam.getFoodProtein())
                .addValue("foodFat", updateParam.getFoodFat())
                .addValue("foodCarbo", updateParam.getFoodCarbo())
                .addValue("foodSugar", updateParam.getFoodSugar())
                .addValue("foodNat", updateParam.getFoodNat())
                .addValue("foodColest", updateParam.getFoodColest())
                .addValue("foodId", foodId);
        template.update(sql, param);
    }


    public Optional<Food> findById(Long id) {
        String sql = "select * from food where food_id = :id";

        try {
            MapSqlParameterSource param = new MapSqlParameterSource("id", id);
            Food food = template.queryForObject(sql, param, foodRowMapper());
            return Optional.of(food);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Food> findAllWithPaging(FoodSearchCond cond, int page, int size) {
        String foodName = cond.getFoodName();

        StringBuilder sql = new StringBuilder("SELECT food_id, food_name, food_kcal, food_protein, food_fat, food_carbo, food_sugar, food_nat, food_colest FROM food");

        // 동적 조건 추가
        if (StringUtils.hasText(foodName)) {
            sql.append(" WHERE food_name LIKE concat('%', :foodName, '%')");
        }
        // 페이징 추가
        sql.append(" ORDER BY food_id ASC"); // 정렬 기준 추가
        sql.append(" LIMIT :size OFFSET :offset"); // LIMIT과 OFFSET 추가


        // 페이징 파라미터 추가
        MapSqlParameterSource pagingParams = new MapSqlParameterSource()
                .addValue("foodName", foodName)
                .addValue("size", size)
                .addValue("offset", page * size); // OFFSET 계산

        log.info("Executing query: {}", sql);
        return template.query(sql.toString(), pagingParams, foodRowMapper());
    }


    public List<Food> findAll(FoodSearchCond cond) {
        String foodName = cond.getFoodName();
        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        StringBuilder sql = new StringBuilder("SELECT food_id, food_name, food_kcal, food_protein, food_fat, food_carbo, food_sugar, food_nat, food_colest FROM food");

        if (StringUtils.hasText(foodName)) {
            sql.append(" WHERE food_name LIKE concat('%', :foodName, '%')");
        }

        log.info("Executing query: {}", sql);
        return template.query(sql.toString(), param, foodRowMapper());
    }

    public void delete(Long foodId) {
        // diet_food 테이블에서 연관된 데이터를 먼저 삭제합니다.
        String deleteDietFoodSql = "DELETE FROM diet_food WHERE food_id = :foodId";
        MapSqlParameterSource params = new MapSqlParameterSource("foodId", foodId);
        template.update(deleteDietFoodSql, params);

        // food 테이블에서 데이터를 삭제합니다.
        String deleteFoodSql = "DELETE FROM food WHERE food_id = :foodId";
        template.update(deleteFoodSql, params);
    }

    private RowMapper<Food> foodRowMapper() {
        return BeanPropertyRowMapper.newInstance(Food.class);
    }

    public int countTotalFoods(FoodSearchCond cond) {
        String foodName = cond.getFoodName();

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM food");

        if (StringUtils.hasText(foodName)) {
            sql.append(" WHERE food_name LIKE concat('%', :foodName, '%')");
        }

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);
        return template.queryForObject(sql.toString(), param, Integer.class);
    }
}
