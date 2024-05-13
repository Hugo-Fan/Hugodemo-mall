package com.hugo.hugodemomall.service;

import com.hugo.hugodemomall.dto.*;
import com.hugo.hugodemomall.model.Member;
import com.hugo.hugodemomall.model.MemberHasRole;
import com.hugo.hugodemomall.model.MemberToken;
import com.hugo.hugodemomall.model.Role;

import java.util.List;

public interface MemberService {
    Member getMemberById(Integer memberId);
    Integer register(MemberRegisterRequest memberRegisterRequest);

    Member getMemberByEmail(String email);

    List<Role> getRoleByMemberId(Integer memberId);

    MemberHasRole createMerchant(Integer memberId);

    MemberToken getMemberTokenByToken(String token);

    void updateMember(Integer memberId, MemberUpdateRequest memberUpdateRequest);

    String forgetPassword(ForgetPasswordRequest forgetPasswordRequest);

    void forgetPdUpdatePd(ForgetUpdatePd forgetUpdatePd);

    void loginUpdatePd(LoginUpdatePd loginUpdatePd);
}
