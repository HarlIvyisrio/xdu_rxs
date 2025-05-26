package com.xdurxs.rxs.kv.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: owl
 * @date: 2025/5/26 15:17
 * @version: v1.0.0
 * @description: 查询笔记内容
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindNoteContentReqDTO {

    @NotBlank(message = "笔记 ID 不能为空")
    private String noteId;

}