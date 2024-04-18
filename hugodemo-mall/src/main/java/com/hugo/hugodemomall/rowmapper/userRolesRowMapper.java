package com.hugo.hugodemomall.rowmapper;

import com.hugo.hugodemomall.model.UserRoles;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class userRolesRowMapper implements RowMapper<UserRoles> {
    @Override
    public UserRoles mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserRoles userRoles = new UserRoles();
        userRoles.setUserId(rs.getInt("user_id"));
        userRoles.setRole_id(rs.getInt("role_id"));
        return userRoles;
    }
}
