package com.tianxue.boot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author tianxue
 * @Date 2020/5/14 2:51 下午
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "boot")
public class SecurityProperties {

    Captch captch=new Captch();

    SmsCode smscode=new SmsCode();

    @Getter
    @Setter
    public class Captch{
        /**
         * 是否开启验证码
         */
        Boolean enable = Boolean.TRUE;

        /**
         * 记住我过期时间s
         */
        int rememberMeExpire = 30;
    }

    @Getter
    @Setter
    public class SmsCode{
        /**
         * 是否开启短信验证码
         */
        Boolean enable = Boolean.TRUE;

        /**
         * 记短信验证码过期时间
         */
        int codeExpire = 60;
    }
}
