package com.nhs.inspection.restaurantscoring.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseDatabaseRepository {

    protected JdbcTemplate jdbcTemplate;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BaseDatabaseRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws SQLException {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public <T> List<T> queryForList(String sql, Object[] args, RowMapper<T> mapper) {
        return jdbcTemplate.query(sql, args, mapper);
    }

    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> mapper) {
        return jdbcTemplate.queryForObject(sql, args, mapper);
    }

    public int deleteObject(String sql, Object[] args) {
        return jdbcTemplate.update(sql, args);
    }

    public int[] insertObject(String sql, List<Map<String, Object>> paramsList) {
        Map<String,Object>[] paramsArray;
        paramsArray = convertLisToArray(paramsList);
        return namedParameterJdbcTemplate.batchUpdate(sql, paramsArray);
    }

    public int[] updateObject(String sql, List<Map<String, Object>> paramsList) {
        Map<String,Object>[] paramsArray;
        paramsArray = convertLisToArray(paramsList);
        return namedParameterJdbcTemplate.batchUpdate(sql, paramsArray);
    }

    private Map<String, Object>[] convertLisToArray(List<Map<String, Object>> paramsList) {
        @SuppressWarnings("unchecked")
        Map<String,Object>[] paramsArray = new HashMap[paramsList.size()];

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
