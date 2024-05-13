package com.hugo.hugodemomall.rowmapper;

import com.hugo.hugodemomall.model.Member;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setEmail(rs.getString("email"));
        member.setPassword(rs.getString("password"));
        member.setName(rs.getString("name"));
        member.setAge(rs.getInt("age"));
        member.setCreated_date(rs.getTimestamp("created_date"));
        member.setLast_modified_date(rs.getTimestamp("last_modified_date"));
        return member;
    }
}
