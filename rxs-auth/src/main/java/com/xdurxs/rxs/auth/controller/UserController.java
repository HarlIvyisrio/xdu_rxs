package com.xdurxs.rxs.auth.controller;

import com.xdurxs.framework.biz.operationlog.aspect.ApiOperationLog;
import com.xdurxs.framework.common.response.Response;
import com.xdurxs.rxs.auth.model.vo.user.UserLoginReqVO;
import com.xdurxs.rxs.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperationLog(description = "用户登录/注册")
    public Response<String> loginAndRegister(@Validated @RequestBody UserLoginReqVO userLoginReqVO) {
        return userService.loginAndRegister(userLoginReqVO);
    }

    @PostMapping("/logout")
    @ApiOperationLog(description = "账号登出")
    public Response<?> logout() {


        log.info("==> 网关透传过来的用户 ID");

        return userService.logout();
    }

}
