package com.tianxue.boot.sms.filtter;

import com.tianxue.boot.exception.CaptchaValidateException;
import com.tianxue.boot.exception.SmsCodeValidateException;
import com.tianxue.boot.processor.AuthAfterHandler;
import com.tianxue.boot.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.tianxue.boot.comm.TxBootConstant.MOBILE;

/**
 * @Author tianxue
 * @Date 2020/5/14 3:46 下午
 */
public class SmsFilter extends OncePerRequestFilter {

    @Autowired
    private AuthAfterHandler.FailureHandler failureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (securityProperties.getSmscode().getEnable()) {
            if (request.getRequestURI().equals("/authentication/code")
                    && StringUtils.equalsIgnoreCase(HttpMethod.POST.name(), request.getMethod())) {
                try {
                    logger.info("自定义短信验证逻辑");
                    SmsCodeValidate(request);
                } catch (SmsCodeValidateException e) {
                    failureHandler.onAuthenticationFailure(request, response, e);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void SmsCodeValidate(HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute(MOBILE);
        String captchaForm = request.getParameter(MOBILE);
        if (captcha == null || captchaForm == null) {
            throw new SmsCodeValidateException("短信验证码为空");
        }
        if (!captcha.equals(captchaForm.toLowerCase())) {
            throw new SmsCodeValidateException("短信验证码错误");
        }
        request.getSession().removeAttribute(MOBILE);
    }
}
