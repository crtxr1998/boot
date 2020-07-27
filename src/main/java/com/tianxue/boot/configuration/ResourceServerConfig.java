package com.tianxue.boot.configuration;

import com.tianxue.boot.processor.AuthAfterHandler;
import com.tianxue.boot.sms.SmsSecurityConfiguration;
import com.tianxue.boot.sms.filtter.SmsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author tianxue
 * @Date 2020/5/18 5:28 下午
 */
@Configuration
@EnableResourceServer   //我是资源服务器
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    /**
     * 登录成功处理器
     */
    @Autowired
    private AuthAfterHandler.SuccessHandler successHandler;

    /**
     * 登录失败处理器
     */
    @Autowired
    private AuthAfterHandler.FailureHandler failureHandler;

    /**
     * 手机登录配置
     */
    @Autowired
    private SmsSecurityConfiguration smsSecurityConfiguration;

    /**
     * 手机登录配置
     */
    @Autowired
    private SmsFilter smsFilter;



    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(smsFilter, UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/authorizeRequest/custom")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .apply(smsSecurityConfiguration)
                .and()
                .authorizeRequests()
                .antMatchers("/login.html",
                        "/authorizeRequest/custom",
                        "/authentication/code",
                        "/captchaCode",
                        "/smsCode")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }
}
