package com.hugo.hugodemomall.security;

import com.hugo.hugodemomall.model.Member;
import com.hugo.hugodemomall.model.Role;
import com.hugo.hugodemomall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class memberDetailsService implements UserDetailsService {
    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 從資料庫中查詢Member資料
        Member member = memberService.getMemberByEmail(username);

        if(member == null){
            throw new UsernameNotFoundException("沒有此使用者:"+username);
        }else {
            String memberEmail = member.getEmail();
            String memberPd = member.getPassword();

            //權限部分
            List<Role> roleList = memberService.getRoleByMemberId(member.getMemberId());
            List<GrantedAuthority> authorityList = convertTOAuthorities(roleList);


            // 轉換成Spring Security 指定的User格式
            return new User(memberEmail,memberPd,authorityList);
        }
    }

    private List<GrantedAuthority> convertTOAuthorities(List<Role> roleList){
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role:roleList){
            authorities.add(new SimpleGrantedAuthority(role.getRoleName())); //直接new SimpleGrantedAuthority 添加角色權限
        }

        return authorities;
    }
}
