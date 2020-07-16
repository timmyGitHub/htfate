package com.htfate.templatecenter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Value("${custom.authorize-ignore-url}")
    private String ignoreUrl;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] ignores = ignoreUrl.split(",");
		/*http
				.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler())
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.cors()
				.and()
				.cors().disable()
				.authorizeRequests()
				.antMatchers("/test2/**").hasAuthority("System");*/
        http
                .authorizeRequests()
                .antMatchers(ignores).permitAll()
                .anyRequest().authenticated().and();
    }
    @Autowired
    private RedisConnectionFactory redisFactory;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //无状态
        resources.stateless(true);
        //设置token存储
        resources.tokenStore(tokenStore());
        // 自定义异常
		resources.authenticationEntryPoint(new MyAuthenticationEntryPoint());
    }

    /**
     * 基于 JDBC 实现，令牌保存到数据库
     */
    /*@Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }*/
    @Bean
    public TokenStore tokenStore() {
        //使用redis存储token
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisFactory);
        //设置redis token存储中的前缀
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }
}
