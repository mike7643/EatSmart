/*
package com.boorisoogeo.EatSmart.repository;

import com.boorisoogeo.EatSmart.domain.Diet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class DietRepository {
    private final NamedParameterJdbcTemplate template;

    public DietRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Diet save(Diet diet) {
        String sql = "insert into diet (breakfast, lunch, dinner) values (:breakfast, :lunch, :dinner)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(diet);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        diet.setId(key);
        return diet;
    }

    public Optional<Diet> findById(Long id) {
        String sql = "select * from diet where id = :id";

        try {
            Map<String, Object> param = Map.of("id", id);
            Diet diet = template.queryForObject(sql, param, dietRowMapper());
            return Optional.of(diet);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Diet> findAll() {
        String sql = "select * from diet";
        return template.query(sql, dietRowMapper());
    }

    private RowMapper<Diet> dietRowMapper() {
        return BeanPropertyRowMapper.newInstance(Diet.class);
    }
}
*/
