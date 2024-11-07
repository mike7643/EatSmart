package com.boorisoogeo.EatSmart.repository;

import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.service.MemberUpdateDto;
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

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class MemberRepository {

    private final NamedParameterJdbcTemplate template;

    public MemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public Member save(Member member) {
        String sql = "insert into member (login_id, name, password, email, gender, date, height, weight) values (:loginId,:name,:password,:email,:gender,:date, :height, :weight)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        member.setId(key);
        return member;
    }

    public void update(Long memberId, MemberUpdateDto updateParam) {
        String sql = "update item set login_id=:loginId, name=:name, password=:password, email=:email, gender=:gender, date=:date, height=:height, weight=:weight" +
                "where id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("loginId", updateParam.getLoginId())
                .addValue("name", updateParam.getName())
                .addValue("password", updateParam.getPassword())
                .addValue("email", updateParam.getEmail())
                .addValue("gender", updateParam.getGender())
                .addValue("date", updateParam.getDate())
                .addValue("height", updateParam.getHeight())
                .addValue("weight", updateParam.getWeight())
                .addValue("id", memberId);
        template.update(sql, param);
    }

    public Optional<Member> findById(Long id) {
        String sql = "select id, login_id, name, password, email, gender from member where id  = :id";

        try {
            Map<String, Object> param = Map.of("id", id);
            Member member = template.queryForObject(sql, param, memberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select id, login_id, name, password, email, gender from member where login_id = :loginId";

        try {
            Map<String, Object> param = Map.of("loginId", loginId);
            Member member = template.queryForObject(sql, param, memberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        String sql = "select id, login_id, name, password, email, gender from member";
        return template.query(sql, memberRowMapper());
    }
    private RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }
}
