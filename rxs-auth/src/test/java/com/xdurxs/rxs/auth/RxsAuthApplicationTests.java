package com.xdurxs.rxs.auth;

import com.xdurxs.framework.common.util.JsonUtil;
import com.xdurxs.rxs.auth.domain.dataobject.UserDO;
import com.xdurxs.rxs.auth.domain.mapper.UserDOMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
class RxsAuthApplicationTests {

    @Resource
    private UserDOMapper userDOMapper;
    @Test
    void testInsert() {
        UserDO userDO = UserDO.builder()
                .username("owl")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        userDOMapper.insert(userDO);
    }
    /**
     * 查询数据
     */
    @Test
    void testSelect() {
        // 查询主键 ID 为 1 的记录
        UserDO userDO = userDOMapper.selectByPrimaryKey(1L);
        log.info("User: {}", JsonUtil.toJsonString(userDO));
    }

    /**
     * 更新数据
     */
    @Test
    void testUpdate() {
        UserDO userDO = UserDO.builder()
                .id(1L)
                .username("lordowl")
                .updateTime(LocalDateTime.now())
                .build();

        // 根据主键 ID 更新记录
        userDOMapper.updateByPrimaryKey(userDO);
    }
    @Test
    void testDelete() {
        // 删除主键 ID 为 4 的记录
        userDOMapper.deleteByPrimaryKey(1L);
    }
}
