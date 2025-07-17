package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    UserMapper userMapper;



    @Autowired
    private WeChatProperties weChatProperties;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return user
     */

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        //调用微信接口服务，获取openid

        String openid = getOpenid(userLoginDTO.getCode());

        //判断openid是否为空

        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        //如果是新用户，自动完成注册

        return user;
    }


    private String getOpenid(String code) {
        //调用微信接口服务，获取openid

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("appid",weChatProperties.getAppid());
        queryParams.put("secret",weChatProperties.getSecret());
        queryParams.put("js_code",code);
        queryParams.put("grant_type","authorization_code");
        String json =HttpClientUtil.doGet(WX_LOGIN_URL, queryParams);

        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
     }
}
