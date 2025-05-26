package com.xdurxs.rxs.user.biz.service;

import com.xdurxs.framework.common.response.Response;
import com.xdurxs.rxs.user.biz.model.vo.UpdateUserInfoReqVO;

public interface UserService {
    /**
     * 更新用户信息
     *
     * @param updateUserInfoReqVO
     * @return
     */
    Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO);
}
