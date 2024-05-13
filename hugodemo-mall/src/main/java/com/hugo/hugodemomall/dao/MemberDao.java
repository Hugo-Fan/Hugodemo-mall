package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.dto.ForgetPasswordRequest;
import com.hugo.hugodemomall.dto.MemberRegisterRequest;
import com.hugo.hugodemomall.dto.MemberUpdateRequest;
import com.hugo.hugodemomall.model.Member;
import com.hugo.hugodemomall.model.MemberHasRole;
import com.hugo.hugodemomall.model.MemberToken;
import com.hugo.hugodemomall.model.Role;

import java.util.List;

public interface MemberDao {

    Member getMemberById(Integer memberId);

    Member getMemberByEmail(String email);

    Integer createMember(MemberRegisterRequest memberRegisterRequest);

    List<Role> getRoleByMemberId(Integer memberId);

    Integer createMemberToken(Integer memberId, String token);

    MemberToken getMemberTokenById(Integer tokenId);

    MemberToken getMemberTokenByMemberId(Integer memberId);

    MemberToken getMemberTokenByToken(String token);

    void updateMemberByPd(String email,String hashPd);

    void updateMemberTokenByTokenId(Integer tokenId,String token);

    void addRoleForMemberId(Integer memberId, Role role);

    MemberHasRole getMemberHasRoleByMemberId(Integer memberId,Integer roleId);

    void updateMember(Integer memberId, MemberUpdateRequest memberUpdateRequest);

    void deleteMemberToken(String token);
}
