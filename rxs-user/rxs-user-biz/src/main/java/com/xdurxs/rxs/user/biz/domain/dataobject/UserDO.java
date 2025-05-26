package com.xdurxs.rxs.user.biz.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDO {
    private Long id;

    private String xiaohashuId;

    private String password;

    private String nickname;

    private String avatar;

    private LocalDate birthday;

    private String backgroundImg;

    private String phone;

    private Integer sex;

    private Integer status;

    private String introduction;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isDeleted;


}