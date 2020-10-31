package com.nhs.inspection.restaurantscoring.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseDatabaseRepository {

    protected JdbcTemplate jdbcTemplate;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BaseDatabaseRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    protected <T> List<T> queryForList(String sql, Object[] args, RowMapper<T> mapper) {
        return jdbcTemplate.query(sql, args, mapper);
    }

    protected <T> T queryForObject(String sql, Object[] args, RowMapper<T> mapper) {
        return jdbcTemplate.queryForObject(sql, args, mapper);
    }

    protected int deleteObject(String sql, Object[] args) {
        return jdbcTemplate.update(sql, args);
    }

    protected int[] insertObject(String sql, List<Map<String, Object>> paramsList) {
        Map<String, Object>[] paramsArray;
        paramsArray = convertLisToArray(paramsList);
        return namedParameterJdbcTemplate.batchUpdate(sql, paramsArray);
    }

    protected int[] updateObject(String sql, List<Map<String, Object>> paramsList) {
        Map<String, Object>[] paramsArray;
        paramsArray = convertLisToArray(paramsList);
        return namedParameterJdbcTemplate.batchUpdate(sql, paramsArray);
    }

    private Map<String, Object>[] convertLisToArray(List<Map<String, Object>> paramsList) {
        @SuppressWarnings("unchecked")
        Map<String, Object>[] paramsArray = new HashMap[paramsList.size()];

        Iterator<Map<String, Object>> iterator = paramsList.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Map<java.lang.String, java.lang.Object> map = (Map<java.lang.String, java.lang.Object>) iterator
                    .next();
            paramsArray[i++] = map;
        }
        return paramsArray;
    }

}
