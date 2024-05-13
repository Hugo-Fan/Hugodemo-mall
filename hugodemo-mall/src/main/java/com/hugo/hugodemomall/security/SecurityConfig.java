package com.hugo.hugodemomall.security;

import com.hugo.hugodemomall.security.Filter.FilterUserLogin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // member使用者密碼加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 設定 Session 的創建機制
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )

                // 設定CSRF 保護
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(creatCsrfHandler())
                        .ignoringRequestMatchers("/members/register", "/members/login","/members/forgetPassword")
                )

//                .csrf(csrf ->csrf.disable())

                // 設定 CORS 跨域
                .cors(cors -> cors
                        .configurationSource(createCorsConfig())
                )


                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())

                .addFilterBefore(new FilterUserLogin(), BasicAuthenticationFilter.class)

                // 設定 api 的權限控制
                .authorizeHttpRequests(request -> request
                                // API文檔
                                .requestMatchers("/mall/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                // member
                                .requestMatchers("/members/register").permitAll()
                                .requestMatchers("/members/forgetPassword").permitAll()
                                .requestMatchers("/members/login").authenticated()
                                .requestMatchers("/members/loginUpdatePd").authenticated()
                                .requestMatchers("/members/orders/**").authenticated()
                                .requestMatchers("/members/createMerchant/**").hasAnyRole("ADMIN", "MALL_MANAGER")

                                // Products
                                .requestMatchers("/products/gets").hasAnyRole("NORMAL_MEMBER", "VIP_MEMBER","ADMIN", "MALL_MANAGER", "MERCHANT")
                                .requestMatchers("/products/get/**").hasAnyRole("NORMAL_MEMBER", "VIP_MEMBER","ADMIN", "MALL_MANAGER", "MERCHANT")
                                .requestMatchers("/products/**").hasAnyRole("ADMIN", "MALL_MANAGER", "MERCHANT")

                                // Order
                                .requestMatchers("/members/**").authenticated()
//                        .requestMatchers("/users/**").hasAnyRole("NORMAL_MEMBER","VIP_MEMBER","ADMIN")

                                .anyRequest().denyAll() // 拒絕所有未設定路徑

                )
                .build();
    }


    private CorsConfigurationSource createCorsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // 後端允許的請求來源有哪些
        config.setAllowedHeaders(List.of("*")); // 後端允許的request header有哪些
        config.setAllowedMethods(List.of("*")); // 後端允許的http method有哪些
        config.setAllowCredentials(true); //後端是否允許前端帶上cookie
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    private CsrfTokenRequestAttributeHandler creatCsrfHandler() {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);

        return csrfHandler;
    }
}
