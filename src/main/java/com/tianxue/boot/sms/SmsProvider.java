package com.tianxue.boot.sms;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author tianxue
 * @Date 2020/5/15 5:30 下午
 */
public class SmsProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public SmsProvider setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        return this;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsToken smsToken= (SmsToken) authentication;
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) smsToken.getPrincipal());
        if (userDetails == null) {
            throw  new InternalAuthenticationServiceException("无法从手机号获取用户信息");
        }
        //认证成功设置userDetails （用户信息）  如果这里设置错误, 后面获取登录用户就会报错
        SmsToken authened=new SmsToken(userDetails,userDetails.getAuthorities());
        //copy Details
        authened.setDetails(smsToken.getDetails());
        return authened;
    }

    /**
     * 是否支持认证
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsToken.class.isAssignableFrom(authentication);
    }
}
