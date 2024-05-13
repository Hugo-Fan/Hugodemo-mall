package com.hugo.hugodemomall.rowmapper;

import com.hugo.hugodemomall.model.MemberToken;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberTokenRoleMapper implements RowMapper<MemberToken> {
    @Override
    public MemberToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        MemberToken memberToken = new MemberToken();
        memberToken.setMemberTokenId(rs.getInt("member_token_id"));
        memberToken.setMemberId(rs.getInt("member_id"));
        memberToken.setToken(rs.getString("token"));
        memberToken.setCreated_date(rs.getTimestamp("created_date"));
        return memberToken;

    }
}
