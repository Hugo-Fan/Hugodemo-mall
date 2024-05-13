package com.hugo.hugodemomall.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HugoMall API Document")
                        .description("以下API不需要登入就能呼叫\n" +
                                "\n\"/members/register\"       註冊會員帳號\n" +
                                "\n\"/members/login\"          帳號登入\n" +
                                "\n\"/members/forgetPassword\" 忘記密碼\n" +
                                "\n提供一組測試帳號 帳號:testMember@gmail.com 密碼:testMember\n"+
                                "\n有開啟CSRF保護除了Get呼叫，請先使用帳號登入API，取得XSRF-TOKEN\n" +
                                "\n![圖片](https://github.com/Hugo-Fan/Hugodemo-mall/assets/163747982/8bcc5099-d3cc-4edd-9a12-5c2cc523cd86)")
                        .version("v0.0.1")
                        .contact(new Contact().name("Hugo").email("excel0617@gmial.com"))
                );
    }
}
