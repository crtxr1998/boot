package com.tianxue.boot.sms.filtter;

import com.tianxue.boot.exception.CaptchaValidateException;
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

import static com.tianxue.boot.comm.TxBootConstant.CAPTCHA;

/**
 * @Author tianxue
 * @Date 2020/5/14 3:46 下午
 */
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private AuthAfterHandler.FailureHandler failureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (securityProperties.getCaptch().getEnable()) {
            if (request.getRequestURI().equals("/authorizeRequest/custom")
                    && StringUtils.equalsIgnoreCase(HttpMethod.POST.name(), request.getMethod())) {
                try {
                    logger.info("自定义验证码验证逻辑");
                    CaptchaValidate(request);
                } catch (CaptchaValidateException e) {
                    failureHandler.onAuthenticationFailure(request, response, e);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void CaptchaValidate(HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute(CAPTCHA);
        String captchaForm = request.getParameter(CAPTCHA);
        logger.info(captcha + "==" + captchaForm);
        if (captcha == null || captchaForm == null) {
            throw new CaptchaValidateException("验证码为空");
        }
        if (!captcha.equals(captchaForm.toLowerCase())) {
            throw new CaptchaValidateException("验证码错误");
        }
        request.getSession().removeAttribute(CAPTCHA);
    }
}
