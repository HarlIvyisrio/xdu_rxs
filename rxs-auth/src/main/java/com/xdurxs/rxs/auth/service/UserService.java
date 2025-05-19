package com.xdurxs.rxs.auth.service;

import com.xdurxs.framework.common.response.Response;
import com.xdurxs.rxs.auth.model.vo.user.UserLoginReqVO;

public interface UserService {
    /**
     * 登录与注册
     * @param userLoginReqVO
     * @return
     */
    Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO);

    /**
     * 退出登录
     * @return
     */
    Response<?> logout(Long userId);
}
