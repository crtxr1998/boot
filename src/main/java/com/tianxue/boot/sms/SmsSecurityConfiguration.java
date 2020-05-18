package com.tianxue.boot.sms;

import com.tianxue.boot.processor.AuthAfterHandler;
import com.tianxue.boot.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class SmsSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity> {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthAfterHandler.SuccessHandler successHandler;

    @Autowired
    private AuthAfterHandler.FailureHandler failureHandler;


    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class ));
        smsAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        smsAuthenticationFilter.setAuthenticationFailureHandler(failureHandler) ;
        SmsProvider smsProvider = new SmsProvider();
        smsProvider.setUserDetailsService(userDetailsService);
        httpSecurity.authenticationProvider(smsProvider).addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
