package com.hugo.hugodemomall.rowmapper;

import com.hugo.hugodemomall.model.MemberHasRole;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberHasRoleMapper implements RowMapper<MemberHasRole> {
    @Override
    public MemberHasRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        MemberHasRole memberHasRole = new MemberHasRole();
        memberHasRole.setMemberHasRoleId(rs.getInt("member_has_role_id"));
        memberHasRole.setMemberId(rs.getInt("member_id"));
        memberHasRole.setRoleId(rs.getInt("role_id"));
        return memberHasRole;
    }
}
