package com.hugo.hugodemomall.controller;

import com.hugo.hugodemomall.dto.*;
import com.hugo.hugodemomall.model.Member;
import com.hugo.hugodemomall.model.MemberHasRole;
import com.hugo.hugodemomall.service.MailService;
import com.hugo.hugodemomall.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@Validated
@Tag(name = "Member", description = "會員有關的Api")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Operation(
            summary = "註冊會員帳號",
            description = "建立會員帳號，並給予會員最基本 普通會員 權限",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "創建會員成功"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "email格式錯誤",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "email已經被註冊",
                            content = @Content
                    )
            }
    )
    @PostMapping("/members/register")
    public ResponseEntity<Member> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {
        // 註冊
        Integer memberId = memberService.register(memberRegisterRequest);

        // 查詢member資料回傳給前端
        Member member = memberService.getMemberById(memberId);

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @Operation(
            summary = "帳號登入",
            description = "帳號登入，並回傳會員基本資料",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登入成功"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "會員帳號或密碼錯誤或未註冊",
                            content = @Content
                    )
            }
    )
    @PostMapping("/members/login")
    public ResponseEntity<Member> login(Authentication authentication) {
        // 驗證登錄已經被Security做過不需要再土炮在寫
        //　取得member 資料
        Member member = memberService.getMemberByEmail(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    @Operation(
            summary = "修改密碼",
            description = "帳號登入後，進行修改密碼",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "已更新會員密碼",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "舊密碼錯誤",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "沒辦法修改其他會員的密码",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "沒有此Email",
                            content = @Content()
                    )

            }
    )
    @PutMapping("/members/loginUpdatePd")
    public ResponseEntity<?> loginUpdatePd(@RequestBody @Valid LoginUpdatePd loginUpdatePd) {
        Member member = memberService.getMemberByEmail(loginUpdatePd.getEmail());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("沒有此Email:" + loginUpdatePd.getEmail());
        }

        if(!passwordEncoder.matches(loginUpdatePd.getOldPassword(),member.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("舊密碼錯誤");
        }

        // 取得目前登入者的email並比對
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getName().equals(loginUpdatePd.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("沒辦法修改其他會員的密码");
        }

        memberService.loginUpdatePd(loginUpdatePd);
        return ResponseEntity.status(HttpStatus.OK).body("已更新會員密碼");
    }

    @Operation(
            summary = "申請商戶權限",
            description = "申請商戶權限，只有MALL_MANAGER以上權限的帳號才能操作",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "已新增商戶權限",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "登入的帳號權限不夠操作",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "memberId不存在",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "memberId已經有商戶資格",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/members/createMerchant")
    public ResponseEntity<?> createMerchant(@RequestBody @Valid CreateMerchantRequest createMerchantRequest) {
        memberService.createMerchant(createMerchantRequest.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "修改會員資料",
            description = "修改會員的基本資料，此API不能修改Email和PassWord",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "已修改會員資料"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "沒有此memberId",
                            content = @Content()
                    )
            }
    )
    @PutMapping("/members/{memberId}")
    public ResponseEntity<Member> updateMember(
            @Parameter(description = "會員ID", required = true)
            @PathVariable Integer memberId,
            @RequestBody @Valid MemberUpdateRequest memberUpdateRequest
    ) {
        // 檢查有沒有此memberId
        Member member = memberService.getMemberById(memberId);
        if (member == null || !member.getEmail().equals(memberUpdateRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // 修改會員資料
        memberService.updateMember(memberId, memberUpdateRequest);

        Member members = memberService.getMemberById(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(members);
    }

    @Operation(
            summary = "忘記密碼",
            description = "忘記密碼會利用會員註冊的Email,寄送一封臨時密碼的郵件",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "已往使用者電子信箱寄送臨時密碼",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "此Email未註冊",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/members/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestBody @Valid ForgetPasswordRequest forgetPasswordRequest) {
        Member member = memberService.getMemberByEmail(forgetPasswordRequest.getEmail());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("此Email未註冊:" + forgetPasswordRequest.getEmail());
        }
        // 產生一組亂數密碼進入資料庫
        String token = memberService.forgetPassword(forgetPasswordRequest);

        // 寄送暫時密碼給會員
        String mail = forgetPasswordRequest.getEmail();
        String subject = "關於您忘記密碼";
        String content = "系統已產生一組臨時密碼，請您登入後修改密碼,臨時密碼: " + token;
        mailService.sendPlainText(mail, subject, content);

        return ResponseEntity.status(HttpStatus.OK).body("已往使用者電子信箱寄送臨時密碼");
    }
}
