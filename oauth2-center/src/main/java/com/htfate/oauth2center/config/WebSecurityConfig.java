package com.htfate.oauth2center.config;

import com.htfate.oauth2center.exception.MyLogoutSuccessHandler;
import com.htfate.oauth2center.exception.OverrideAuthorizeFailureException;
import com.htfate.oauth2center.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OverrideAuthorizeFailureException authorizeException;
    @Autowired
    private MyLogoutSuccessHandler logoutSuccessHandler;
//    @Autowired
//    private OverrideAuthorizeSuccessException authorizeSuccessException;

    @Value("${custom.redirect-login-url}")
    private String redirectLoginUrl;
    @Value("${custom.redirect-process-login-url}")
    private String redirectProcessLoginUrl;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义认证与授权
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.formLogin().failureHandler(authorizeException);
        http.logout().
                logoutRequestMatcher(new AntPathRequestMatcher("/sys/logout", "GET"))
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
        ;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // 将 check_token 暴露出去，否则资源服务器访问时报 403 错误
        web.ignoring().antMatchers("/oauth/check_token");
    }
    /*@Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/login")
                .failureHandler(authorizeException)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/login","/custom/confirm_access","/oauth/token").permitAll()
                .anyRequest().authenticated().and()
                .csrf().disable();
    }*/
}
