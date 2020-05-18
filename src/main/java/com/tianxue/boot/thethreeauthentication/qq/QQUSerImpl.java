package com.tianxue.boot.thethreeauthentication.qq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * @Author tianxue
 * @Date 2020/5/16 6:00 下午
 */
public class QQUSerImpl extends AbstractOAuth2ApiBinding  implements QQ{

    private String clientId;
    private String openid;
    private ObjectMapper objectMapper=new ObjectMapper();

    public QQUSerImpl(String accessToken,String clientId) {
        this.clientId = clientId;
    }


    @Override
    public QQUerInfo getUserInfo() {
        return null;
    }
}
