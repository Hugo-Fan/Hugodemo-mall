package com.hugo.hugodemomall.dao.impl;

import com.hugo.hugodemomall.dao.MemberDao;
import com.hugo.hugodemomall.dto.ForgetPasswordRequest;
import com.hugo.hugodemomall.dto.MemberRegisterRequest;
import com.hugo.hugodemomall.dto.MemberUpdateRequest;
import com.hugo.hugodemomall.model.Member;
import com.hugo.hugodemomall.model.MemberHasRole;
import com.hugo.hugodemomall.model.MemberToken;
import com.hugo.hugodemomall.model.Role;
import com.hugo.hugodemomall.rowmapper.MemberHasRoleMapper;
import com.hugo.hugodemomall.rowmapper.MemberRowMapper;
import com.hugo.hugodemomall.rowmapper.MemberTokenRoleMapper;
import com.hugo.hugodemomall.rowmapper.RoleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MemberDaoImpl implements MemberDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Member getMemberByEmail(String email) {
        String sql = """
                SELECT  member_id,email,password,name,age,created_date,last_modified_date
                FROM member
                WHERE email = :email
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        List<Member> membersList = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

        if (membersList.size() > 0) {
            return membersList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Member getMemberById(Integer memberId) {
        String sql = """
                SELECT  member_id,email,password,name,age,created_date,last_modified_date
                FROM member
                WHERE member_id=:memberId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        List<Member> membersList = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

        if (membersList.size() > 0) {
            return membersList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createMember(MemberRegisterRequest memberRegisterRequest) {
        String sql = """
                INSERT INTO member(email,password,name,age,created_date,last_modified_date)
                VALUES (:email, :password, :name, :age,:createdDate, :lastModifiedDate)
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("email", memberRegisterRequest.getEmail());
        map.put("password", memberRegisterRequest.getPassword());
        map.put("name", memberRegisterRequest.getName());
        map.put("age", memberRegisterRequest.getAge());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int memberId = keyHolder.getKey().intValue();

        return memberId;
    }

    @Override
    public List<Role> getRoleByMemberId(Integer memberId) {
        String sql = """
                SELECT role.role_id, role.role_name FROM role
                    JOIN member_has_role ON role.role_id = member_has_role.role_id
                    WHERE member_has_role.member_id = :memberId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        List<Role> roleList = namedParameterJdbcTemplate.query(sql, map, new RoleRowMapper());

        return roleList;
    }

    @Override
    public void addRoleForMemberId(Integer memberId, Role role) {
        String sql = "INSERT INTO member_has_role(member_id, role_id) VALUES (:memberId, :roleId)";

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("roleId", role.getRoleId());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public MemberHasRole getMemberHasRoleByMemberId(Integer memberId, Integer roleId) {
        String sql = """
                SElECT member_has_role_id,member_id,role_id
                FROM member_has_role
                WHERE member_id = :memberId AND role_id = :roleId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("roleId", roleId);

        List<MemberHasRole> hasRoleList = namedParameterJdbcTemplate.query(sql, map, new MemberHasRoleMapper());

        if (hasRoleList.size() > 0) {
            return hasRoleList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void updateMember(Integer memberId, MemberUpdateRequest memberUpdateRequest) {
        String sql = """
                UPDATE member
                SET name =:name,age = :age ,last_modified_date = :lastModifiedDate
                WHERE member_id = :memberId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("name", memberUpdateRequest.getName());
        map.put("age", memberUpdateRequest.getAge());
        map.put("memberId", memberId);
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public Integer createMemberToken(Integer memberId, String token) {
        String sql = """
                INSERT INTO member_token(member_id,token,created_date)
                VALUES (:memberId,:token,:createdDate)
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("token", token);
        Date now = new Date();
        map.put("createdDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int memberTokenId = keyHolder.getKey().intValue();

        return memberTokenId;
    }


    @Override
    public MemberToken getMemberTokenById(Integer tokenId) {
        String sql = """
                SElECT member_token_id,member_id,token,created_date
                FROM  member_token
                WHERE member_token_id = :tokenId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", tokenId);

        List<MemberToken> memberTokenList = namedParameterJdbcTemplate.query(sql, map, new MemberTokenRoleMapper());

        if (memberTokenList.size() > 0) {
            return memberTokenList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public MemberToken getMemberTokenByToken(String token) {
        String sql = """
                SElECT member_token_id,member_id,token,created_date
                FROM  member_token
                WHERE token = :token
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);

        List<MemberToken> memberTokenList = namedParameterJdbcTemplate.query(sql, map, new MemberTokenRoleMapper());

        if (memberTokenList.size() > 0) {
            return memberTokenList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public MemberToken getMemberTokenByMemberId(Integer memberId) {
        String sql = """
                SElECT member_token_id,member_id,token,created_date
                FROM  member_token
                WHERE member_id = :memberId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);

        List<MemberToken> memberTokenList = namedParameterJdbcTemplate.query(sql, map, new MemberTokenRoleMapper());

        if (memberTokenList.size() > 0) {
            return memberTokenList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void updateMemberTokenByTokenId(Integer tokenId, String token) {
        String sql = """
                UPDATE member_token
                SET token = :token,created_date = :createdDate
                WHERE member_token_id = :tokenId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", tokenId);
        map.put("token", token);
        map.put("createdDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void updateMemberByPd(String email, String hashPd) {
        String sql = """
                UPDATE member
                SET password = :hashPd,last_modified_date = :lastModifiedDate
                WHERE email = :email
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("hashPd", hashPd);
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteMemberToken(String token) {
        String sql = """
                DELETE FROM member_token
                WHERE token = :token
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
