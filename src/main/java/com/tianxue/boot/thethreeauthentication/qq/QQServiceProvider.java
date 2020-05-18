package com.tianxue.boot.thethreeauthentication.qq;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * @Author tianxue
 * @Date 2020/5/16 9:18 下午
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String clientId;

    private static String authorizeUrl = "https://graph.qq.com/oauth2.0/authorize";
    private static String accessTokenUrl = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String clientId, String clientSecret) {
        super(new OAuth2Template(clientId, clientSecret,authorizeUrl,accessTokenUrl));
        this.clientId = clientId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQUSerImpl(accessToken, clientId);
    }
}
