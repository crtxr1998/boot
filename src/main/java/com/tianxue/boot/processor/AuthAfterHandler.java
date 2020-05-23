package com.tianxue.boot.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.web.authentication.www.BasicAuthenticationConverter.AUTHENTICATION_SCHEME_BASIC;

/**
 * 认证之后的处理器
 */
@Configuration
public class AuthAfterHandler {

    private static Logger logger = LoggerFactory.getLogger(AuthAfterHandler.class);


    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;



    private ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 认证成功处理器
     */
    @Component
    public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            logger.info("onAuthenticationSuccess");

            String header = request.getHeader(AUTHORIZATION);
            if (header == null || !StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BASIC)) {
                throw new BadCredentialsException("Request don't have" + AUTHENTICATION_SCHEME_BASIC + "Info");
            }

            byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
            byte[] decoded;
            try {
                decoded = Base64.getDecoder().decode(base64Token);
            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException(
                        "Failed to decode basic authentication token");
            }
            String token = new String(decoded, StandardCharsets.UTF_8);
            int delim = token.indexOf(":");
            if (delim == -1) {
                throw new BadCredentialsException("Invalid basic authentication token");
            }
            String client_id = token.substring(0, delim);
            String client_secret = token.substring(delim + 1);

            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(client_id);
            if (clientDetails == null) {
                throw new UnapprovedClientAuthenticationException(
                        "Client_Id  not exist ");
            } else if (!clientDetails.getClientSecret().equals(client_secret)) {
                throw new UnapprovedClientAuthenticationException(
                        "Client_Secret  not match ");
            }
            //参数Map是为了区构建Authentication ,这里已经有认证好的authentication了就直接传入空mao
            TokenRequest tokenRequest = new TokenRequest(new HashMap<>(),client_id,clientDetails.getScope(),"custom");

            OAuth2Request oAuth2Request=tokenRequest.createOAuth2Request(clientDetails);

            OAuth2Authentication auth2Authentication=new OAuth2Authentication(oAuth2Request,authentication);


            OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(auth2Authentication);

            response.setContentType("application/json;chasrset=utf-8");

            response.getWriter().write(objectMapper.writeValueAsString(accessToken));
          //  super.onAuthenticationSuccess(request, response, authentication);
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
