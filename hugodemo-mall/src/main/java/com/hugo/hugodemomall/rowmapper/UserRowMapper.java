package com.hugo.hugodemomall.rowmapper;

import com.hugo.hugodemomall.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreated_date(rs.getTimestamp("created_date"));
        user.setLast_modified_date(rs.getTimestamp("last_modified_date"));
        return user;
    }
}
