package com.hugo.hugodemomall.service.impl;

import com.hugo.hugodemomall.dao.MemberDao;
import com.hugo.hugodemomall.dao.RoleDao;
import com.hugo.hugodemomall.dto.*;
import com.hugo.hugodemomall.model.Member;
import com.hugo.hugodemomall.model.MemberHasRole;
import com.hugo.hugodemomall.model.MemberToken;
import com.hugo.hugodemomall.model.Role;
import com.hugo.hugodemomall.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;

@Component
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Override
    public Member getMemberById(Integer memberId) {
        return memberDao.getMemberById(memberId);
    }

    @Transactional
    @Override
    public Integer register(MemberRegisterRequest memberRegisterRequest) {
        // 檢查email 有沒有重複註冊
        Member member = memberDao.getMemberByEmail(memberRegisterRequest.getEmail());

        if(member!=null){
            log.warn("該 email {} 已經被註冊",memberRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        // 密碼加密
        String hashPd = passwordEncoder.encode(memberRegisterRequest.getPassword());
        memberRegisterRequest.setPassword(hashPd);

        // 創建使用者
        Integer memberId = memberDao.createMember(memberRegisterRequest);

        // 給予預設權限
        Role normalRole = roleDao.getRoleByName("ROLE_NORMAL_MEMBER");
        memberDao.addRoleForMemberId(memberId,normalRole);

        return memberId;
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberDao.getMemberByEmail(email);
    }

    @Override
    public List<Role> getRoleByMemberId(Integer memberId) {
        return memberDao.getRoleByMemberId(memberId);
    }

    @Override
    public MemberHasRole createMerchant(Integer memberId) {
        //
        Member member = memberDao.getMemberById(memberId);

        if(member==null){
            log.warn(" {} memberId 不存在",memberId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Role merchantRole = roleDao.getRoleByName("ROLE_MERCHANT");

        // 查詢會員是否已經有商戶資格
        MemberHasRole memberHasRole = memberDao.getMemberHasRoleByMemberId(memberId,merchantRole.getRoleId());

        if(memberHasRole == null){
            memberDao.addRoleForMemberId(memberId,merchantRole);
        }else {
            log.warn(" {} memberId 已經有商戶資格",memberId);
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        //再次查詢會員是否已經有商戶資格
        MemberHasRole reMemberHasRole = memberDao.getMemberHasRoleByMemberId(memberId,merchantRole.getRoleId());

        return reMemberHasRole;
    }

    // 更新member數據
    @Override
    public void updateMember(Integer memberId, MemberUpdateRequest memberUpdateRequest) {
        memberDao.updateMember(memberId, memberUpdateRequest);
    }

    // 忘記密碼寫入一組亂數密碼
    @Override
    public String forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        // 亂數產生一組token
        String token = generatePassword(10);
        String hashPd = passwordEncoder.encode(token);
        memberDao.updateMemberByPd(forgetPasswordRequest.getEmail(),hashPd);
        return token;
    }
    @Transactional
    @Override
    public void forgetPdUpdatePd(ForgetUpdatePd forgetUpdatePd) {
        String hashPd = passwordEncoder.encode(forgetUpdatePd.getNewPassword());
        memberDao.updateMemberByPd(forgetUpdatePd.getEmail(),hashPd);
        memberDao.deleteMemberToken(forgetUpdatePd.getToken());
    }

    @Override
    public void loginUpdatePd(LoginUpdatePd loginUpdatePd) {
        String hashNewPd = passwordEncoder.encode(loginUpdatePd.getNewPassword());
        memberDao.updateMemberByPd(loginUpdatePd.getEmail(),hashNewPd);
    }

    @Override
    public MemberToken getMemberTokenByToken(String token) {
        return memberDao.getMemberTokenByToken(token);
    }

    private String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }
}
