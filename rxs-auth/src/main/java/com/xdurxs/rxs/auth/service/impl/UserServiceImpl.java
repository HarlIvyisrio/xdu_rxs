package com.xdurxs.rxs.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.xdurxs.rxs.auth.domain.dataobject.RoleDO;
import com.xdurxs.rxs.auth.domain.dataobject.UserRoleDO;
import com.xdurxs.framework.common.eumns.DeletedEnum;
import com.xdurxs.framework.common.eumns.StatusEnum;
import com.xdurxs.framework.common.exception.BizException;
import com.xdurxs.framework.common.response.Response;
import com.xdurxs.framework.common.util.JsonUtil;
import com.xdurxs.rxs.auth.constant.RedisKeyConstants;
import com.xdurxs.rxs.auth.constant.RoleConstants;
import com.xdurxs.rxs.auth.domain.dataobject.UserDO;
import com.xdurxs.rxs.auth.domain.mapper.RoleDOMapper;
import com.xdurxs.rxs.auth.domain.mapper.UserDOMapper;
import com.xdurxs.rxs.auth.domain.mapper.UserRoleDOMapper;
import com.xdurxs.rxs.auth.enums.LoginTypeEnum;
import com.xdurxs.rxs.auth.enums.ResponseCodeEnum;
import com.xdurxs.rxs.auth.filter.LoginUserContextHolder;
import com.xdurxs.rxs.auth.model.vo.user.UserLoginReqVO;
import com.xdurxs.rxs.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserRoleDOMapper userRoleDOMapper;

    @Resource
    private RoleDOMapper roleDOMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();

        log.info("==> 用户退出登录, userId: {}", userId);

        // 退出登录 (指定用户 ID)
        StpUtil.logout(userId);

        return Response.success();
    }
    /**
     * 登录与注册
     *
     * @param userLoginReqVO
     * @return
     */
    @Override
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.getPhone();
        Integer type = userLoginReqVO.getType();

        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);

        Long userId = null;

        // 判断登录类型
        switch (loginTypeEnum) {
            case VERIFICATION_CODE: // 验证码登录
                String verificationCode = userLoginReqVO.getCode();

                // 校验入参验证码是否为空
                if (StringUtils.isBlank(verificationCode)) {
                    return Response.fail(ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode(), "验证码不能为空");
                }

                // 构建验证码 Redis Key
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);
                // 查询存储在 Redis 中该用户的登录验证码
                String sentCode = (String) redisTemplate.opsForValue().get(key);

                // 判断用户提交的验证码，与 Redis 中的验证码是否一致
                if (!StringUtils.equals(verificationCode, sentCode)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }

                // 通过手机号查询记录
                UserDO userDO = userDOMapper.selectByPhone(phone);

                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtil.toJsonString(userDO));

                // 判断是否注册
                if (Objects.isNull(userDO)) {

                    // 若此用户还没有注册，系统自动注册该用户
                    userId = registerUser(phone);

                } else {
                    // 已注册，则获取其用户 ID
                    userId = userDO.getId();
                    // 查询用户角色
                    List<UserRoleDO> userRoles = new ArrayList<>();
                    userRoles.add(userRoleDOMapper.selectByPrimaryKey(userId));
                    if (CollUtil.isNotEmpty(userRoles)) {
                        List<String> roles = userRoles.stream()
                                .map(role -> {
                                    RoleDO roleDO = roleDOMapper.selectByPrimaryKey(role.getRoleId());
                                    return roleDO.getRoleKey();
                                }).toList();

                        String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
                        redisTemplate.opsForValue().set(userRolesKey, JsonUtil.toJsonString(roles));
                    }

                }
                break;
            case PASSWORD: // 密码登录
                // todo

                break;
            default:
                break;
        }

        // SaToken 登录用户，并返回 token 令牌

        StpUtil.login(userId);

        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();

        return Response.success(saTokenInfo.tokenValue);
    }

    /**
     * 系统自动注册用户
     * @param phone
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                // 获取全局自增的小哈书 ID
                Long xiaohashuId = redisTemplate.opsForValue().increment(RedisKeyConstants.XIAOHASHU_ID_GENERATOR_KEY);

                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .xiaohashuId(String.valueOf(xiaohashuId)) // 自动生成小红书号 ID
                        .nickname("电兵" + xiaohashuId) // 自动生成昵称, 如：电兵10000
                        .status(StatusEnum.ENABLE.getValue()) // 状态为启用
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue()) // 逻辑删除
                        .build();

                // 添加入库
                userDOMapper.insert(userDO);

                // 获取刚刚添加入库的用户 ID
                Long userId = userDO.getId();

                // 给该用户分配一个默认角色
                UserRoleDO userRoleDO = UserRoleDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue())
                        .build();
                userRoleDOMapper.insert(userRoleDO);

                RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

                // 将该用户的角色 ID 存入 Redis 中，指定初始容量为 1，这样可以减少在扩容时的性能开销
                List<String> roles = new ArrayList<>(1);
                roles.add(roleDO.getRoleKey());

                String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
                redisTemplate.opsForValue().set(userRolesKey, JsonUtil.toJsonString(roles));

                return userId;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("==》系统注册用户异常", e);
                return null;
            }
        });

    }
}
