package com.tianxue.boot.configuration;

import com.tianxue.boot.processor.AuthAfterHandler;
import com.tianxue.boot.processor.UserAuthProcessor;
import com.tianxue.boot.properties.SecurityProperties;
import com.tianxue.boot.sms.SmsSecurityConfiguration;
import com.tianxue.boot.sms.filtter.CaptchaFilter;
import com.tianxue.boot.sms.filtter.SmsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserAuthProcessor();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CaptchaFilter captchaFilter() {
        return new CaptchaFilter();
    }

    @Bean
    public SmsFilter smsFilter() {
        return new SmsFilter();
    }


    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;


    /**
     * 记住我功能
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }


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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(smsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/authorizeRequest/custom")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .apply(smsSecurityConfiguration)
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getCaptch().getRememberMeExpire())
                .userDetailsService(userDetailsService)
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
