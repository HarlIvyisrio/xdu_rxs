package com.xdurxs.framework.common.eumns;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: owl
 * @url: www.quanxiaoha.com
 * @date: 2025-05-16
 * @description: 逻辑删除
 **/
@Getter
@AllArgsConstructor
public enum DeletedEnum {

    YES(true),
    NO(false);

    private final Boolean value;
}