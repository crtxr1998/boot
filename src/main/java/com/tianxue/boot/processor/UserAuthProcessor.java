package com.tianxue.boot.processor;

import com.tianxue.boot.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;

public class UserAuthProcessor implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        HashMap<String,UserDetails> hashMap=new HashMap<>();
        hashMap.put("tianxue", new UserInfo().setUsername("tianxue")
                .setPassword(passwordEncoder.encode("1234567"))
                .setLock(true)
                .setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("tianxue")));
        hashMap.put("15773370817", new UserInfo().setUsername("15773370817")
                .setPassword(passwordEncoder.encode("1111111"))
                .setLock(true)
                .setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("smscode")));


        UserDetails userInfo = hashMap.get(s);
        if (userInfo==null){
            throw new UsernameNotFoundException("用户名密码错误");

        }        return userInfo;
    }
}
