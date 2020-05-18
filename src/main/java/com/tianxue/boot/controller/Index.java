package com.tianxue.boot.controller;

import com.tianxue.boot.user.UserInfo;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.tianxue.boot.comm.TxBootConstant.CAPTCHA;
import static com.tianxue.boot.comm.TxBootConstant.MOBILE;

@RestController
public class Index {

    @Autowired
    private PersistentTokenRepository tokenRepository;

    @RequestMapping("/index")
    public String hello(@RequestParam(required = false) String nameq) {
        return "hello: " + nameq;
    }

    @RequestMapping("/user")
    public UserInfo user(@AuthenticationPrincipal UserInfo userInfo) {
        System.out.println(userInfo.getUsername()+"us");
        return userInfo;
    }

    /**
     * 登出接口
     * @param userInfo
     * @return
     */
    @RequestMapping("/out")
    public String out(@AuthenticationPrincipal UserInfo userInfo) {
        tokenRepository.removeUserTokens(userInfo.getUsername());
        return userInfo.getUsername()+"退出";
    }


    @RequestMapping("/smsCode")
    public void smsCode(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setDateHeader("Expires", 0);
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        System.out.println("短信验证码：："+specCaptcha.text());
        // 验证码存入session
        request.getSession().setAttribute(MOBILE, specCaptcha.text().toLowerCase());

    }


    @RequestMapping("/captchaCode")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

        // 验证码存入session
        request.getSession().setAttribute(CAPTCHA, specCaptcha.text().toLowerCase());
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

}
