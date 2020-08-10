package com.yxgy.controller;

import com.yxgy.pojo.Users;
import com.yxgy.pojo.vo.UsersVO;
import com.yxgy.service.UserService;
import com.yxgy.utils.JSONResult;
import com.yxgy.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value = "用户注册和登陆的接口", tags = {"登陆和注册的Controller"})
public class RegisterLoginController extends BasicController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户注册的接口")
    public JSONResult register(@RequestBody Users user) throws Exception {

        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return JSONResult.errorMsg("用户名和密码不能为空");
        }

        if (!userService.queryUsernameIsExist(user.getUsername())) {
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } else {
            //已经存在
            return JSONResult.errorMsg("用户名已存在，请换一个重试");
        }

        user.setPassword("");

        UsersVO userVO = setUserRedisSessionToken(user);

        return JSONResult.ok(userVO);
    }

    public UsersVO setUserRedisSessionToken(Users userModel) {
        String uniqueToken = UUID.randomUUID().toString();
        redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);

        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userModel, userVO);
        userVO.setUserToken(uniqueToken);
        return userVO;
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登陆", notes = "用户登陆的接口")
    public JSONResult login(@RequestBody Users user) throws Exception {

        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return JSONResult.errorMsg("用户名和密码不能为空");
        }

        Users result = userService.queryUserForLogin(user.getUsername(), MD5Utils.getMD5Str(user.getPassword()));
        if (result != null) {
            result.setPassword("");
            UsersVO userVO = setUserRedisSessionToken(result);
            return JSONResult.ok(userVO);
        } else {
            return JSONResult.errorMsg("用户名或密码错误");
        }
    }

    @ApiOperation(value="用户注销", notes="用户注销的接口")
    @ApiImplicitParam(name="userId", value="用户id", required=true,
            dataType="String", paramType="query")
    @PostMapping("/logout")
    public JSONResult logout(String userId) throws Exception {
        redis.del(USER_REDIS_SESSION + ":" + userId);
        return JSONResult.ok();
    }


}
