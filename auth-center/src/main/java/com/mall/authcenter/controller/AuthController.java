package com.mall.authcenter.controller;

import com.mall.authcenter.utils.JwtTokenUtil;
import com.mall.authcenter.utils.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 获取授权token
     * @param userId
     * @param userName
     * @param userPwd
     * @return
     */
    @GetMapping("/getAuthToken")
    public  Object getAuthToken(String userId, String userName,String userPwd){
        try
        {
            String token = JwtTokenUtil.createToken(userId,userName,userPwd);
            return  ResponseUtil.ok(token);
        }
        catch (Exception ex){

        }
        return ResponseUtil.fail();
    }

    /**
     * 验证token 是否有效
     * @param token
     * @return
     */
    public  Object vertifyAuthToken(String token,String userId){

        try {
             Boolean bValid = JwtTokenUtil.verifyToken(token,userId);
             return ResponseUtil.ok();

        }catch (Exception ex){

        }
        return ResponseUtil.fail();
    }
    
}
