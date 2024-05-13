package com.hugo.hugodemomall.dao.impl;

import com.hugo.hugodemomall.dao.RoleDao;
import com.hugo.hugodemomall.model.Role;
import com.hugo.hugodemomall.rowmapper.RoleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Role getRoleByName(String roleName) {
        String sql = "SELECT role_id, role_name FROM role WHERE role_name = :roleName";

        Map<String, Object> map = new HashMap<>();
        map.put("roleName", roleName);

        List<Role> roleList = namedParameterJdbcTemplate.query(sql, map, new RoleRowMapper());

        if (roleList.isEmpty()) {
            return null;
        } else {
            return roleList.get(0);
        }
    }
}
