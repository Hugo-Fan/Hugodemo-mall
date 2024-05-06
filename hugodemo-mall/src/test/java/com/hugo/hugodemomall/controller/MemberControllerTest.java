package com.hugo.hugodemomall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugo.hugodemomall.constant.ProductCategory;
import com.hugo.hugodemomall.dao.MemberDao;
import com.hugo.hugodemomall.dto.*;
import com.hugo.hugodemomall.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberDao memberDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 註冊新帳號
    @Test
    @Transactional
    public void register_success() throws Exception {
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test1@gmail.com");
        memberRegisterRequest.setPassword("123");
        memberRegisterRequest.setName("Hugo");
        memberRegisterRequest.setAge(28);


        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.memberId", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.age", notNullValue()))
                .andExpect(jsonPath("$.created_date", notNullValue()))
                .andExpect(jsonPath("$.last_modified_date", notNullValue()));

        // 檢查資料庫中的密碼不為明碼
        Member member = memberDao.getMemberByEmail(memberRegisterRequest.getEmail());
        assertNotEquals(memberRegisterRequest.getPassword(), member.getPassword());

    }

    // 註冊一個信箱亂填的
    @Test
    public void register_invalidEmailFormat() throws Exception {
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("3gd8e7q34l9");
        memberRegisterRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 重複註冊
    @Test
    @Transactional
    public void register_emailAlreadyExist() throws Exception {
        // 先註冊一個帳號
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test2@gmail.com");
        memberRegisterRequest.setPassword("123");

        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        // 再次使用同個 email 註冊
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    // 登入
    @Test
    public void login_success() throws Exception {
        // 先註冊新帳號
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test1@gmail.com");
        memberRegisterRequest.setPassword("123");

        register(memberRegisterRequest);


        // 再測試登入功能
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/login")
                .with(httpBasic(memberRegisterRequest.getEmail(), memberRegisterRequest.getPassword()));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.memberId", notNullValue()))
                .andExpect(jsonPath("$.email", equalTo(memberRegisterRequest.getEmail())))
                .andExpect(jsonPath("$.created_date", notNullValue()))
                .andExpect(jsonPath("$.last_modified_date", notNullValue()));
    }

    // 輸入錯誤的登入密碼
    @Test
    public void login_wrongPassword() throws Exception {
        // 先註冊新帳號
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test4@gmail.com");
        memberRegisterRequest.setPassword("123");


        register(memberRegisterRequest);

        // 測試密碼輸入錯誤的情況

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/login")
                .with(httpBasic(memberRegisterRequest.getEmail(), "456"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }

    // 使用錯誤的email格式
    @Test
    public void login_invalidEmailFormat() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/login")
                .with(httpBasic("hkbudsr324", "123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }

    // 輸入未註冊帳號
    @Test
    public void login_emailNotExist() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/login")
                .with(httpBasic("unknown@gmail.com", "123"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(401));
    }

    // 申請商戶
    @Test
    @Transactional
    public void createMerchant_success() throws Exception {
        CreateMerchantRequest createMerchantRequest = new CreateMerchantRequest();
        createMerchantRequest.setMemberId(1);

        String memberJson = objectMapper.writeValueAsString(createMerchantRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/createmerchant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(memberJson)
                .with(httpBasic("admin@gmail.com", "admin"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.memberHasRoleId", notNullValue()))
                .andExpect(jsonPath("$.memberId",equalTo(1)))
                .andExpect(jsonPath("$.roleId", notNullValue()));

        // 測試能不能添加商品
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("http://test.com");
        productRequest.setPrice(100);
        productRequest.setStock(2);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder productRequestBuilder = MockMvcRequestBuilders
                .post("/products/create/{userId}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("user1@gmail.com", "user1"))
                .with(csrf());

        mockMvc.perform(productRequestBuilder)
                .andExpect(status().is(201))
                .andDo(print())
                .andExpect(jsonPath("$.productName", equalTo("test food product")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", equalTo("http://test.com")))
                .andExpect(jsonPath("$.price", equalTo(100)))
                .andExpect(jsonPath("$.stock", equalTo(2)))
                .andExpect(jsonPath("$.description", nullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));

    }

    // 申請商戶操作的帳戶權限不夠
    @Test
    public void createMerchant_InsufficientPermissions() throws Exception {
        CreateMerchantRequest createMerchantRequest = new CreateMerchantRequest();
        createMerchantRequest.setMemberId(1);

        String memberJson = objectMapper.writeValueAsString(createMerchantRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/createmerchant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(memberJson)
                .with(httpBasic("user1@gmail.com", "user1"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));

    }

    // 申請商戶的memberId不存在
    @Test
    @Transactional
    public void createMerchant_memberIdNotExist() throws Exception {
        CreateMerchantRequest createMerchantRequest = new CreateMerchantRequest();
        createMerchantRequest.setMemberId(100);

        String memberJson = objectMapper.writeValueAsString(createMerchantRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/createmerchant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(memberJson)
                .with(httpBasic("admin@gmail.com", "admin"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));

    }

    // 更新member資料
    @Transactional
    @Test
    public void updateMember_success() throws Exception{
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.setEmail("user1@gmail.com");
        memberUpdateRequest.setName("test");
        memberUpdateRequest.setAge(18);

        String json = objectMapper.writeValueAsString(memberUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/members/{memberId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("user1@gmail.com", "user1"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andDo(print())
                .andExpect(jsonPath("$.memberId",notNullValue()))
                .andExpect(jsonPath("$.email",equalTo(memberUpdateRequest.getEmail())))
                .andExpect(jsonPath("$.name",notNullValue()))
                .andExpect(jsonPath("$.age",notNullValue()))
                .andExpect(jsonPath("$.created_date",notNullValue()))
                .andExpect(jsonPath("$.last_modified_date",notNullValue()));
    }

    // 更新member資料_帶入不同會員Email
    @Test
    public void updateMember_EmailError() throws Exception{
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.setEmail("admin@gmail.com");
        memberUpdateRequest.setName("test");
        memberUpdateRequest.setAge(18);

        String json = objectMapper.writeValueAsString(memberUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/members/{memberId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("user1@gmail.com", "user1"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }

    // 更新member資料_不存在的memberId
    @Test
    public void updateMember_memberIdNotExist() throws Exception{
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.setEmail("user1@gmail.com");
        memberUpdateRequest.setName("test");
        memberUpdateRequest.setAge(18);

        String json = objectMapper.writeValueAsString(memberUpdateRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/members/{memberId}",10000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(httpBasic("user1@gmail.com", "user1"))
                .with(csrf());

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }

    private void register(MemberRegisterRequest memberRegisterRequest) throws Exception {
        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));
    }
}