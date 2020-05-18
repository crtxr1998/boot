package com.tianxue.boot.processor;

import com.tianxue.boot.user.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证之后的处理器
 */
@Configuration
public class AuthAfterHandler {

    private static Logger logger = LoggerFactory.getLogger(AuthAfterHandler.class);

    /**
     * 认证成功处理器
     */
    @Component
    public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            logger.info("onAuthenticationSuccess");
            System.out.println((authentication.getPrincipal()).toString());
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    /**
     * 认证失败处理器
     */
    @Component
    public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            logger.info(exception);
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
