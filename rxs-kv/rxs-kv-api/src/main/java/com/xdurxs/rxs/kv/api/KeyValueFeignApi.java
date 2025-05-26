package com.xdurxs.rxs.kv.api;

import com.xdurxs.framework.common.response.Response;
import com.xdurxs.rxs.kv.constant.ApiConstants;
import com.xdurxs.rxs.kv.dto.req.AddNoteContentReqDTO;
import com.xdurxs.rxs.kv.dto.req.DeleteNoteContentReqDTO;
import com.xdurxs.rxs.kv.dto.req.FindNoteContentReqDTO;
import com.xdurxs.rxs.kv.dto.rsp.FindNoteContentRspDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: owl
 * @date: 2025/5/26 22:56
 * @version: v1.0.0
 * @description: K-V 键值存储 Feign 接口
 **/
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface KeyValueFeignApi {

    String PREFIX = "/kv";

    @PostMapping(value = PREFIX + "/note/content/add")
    Response<?> addNoteContent(@RequestBody AddNoteContentReqDTO addNoteContentReqDTO);

    @PostMapping(value = PREFIX + "/note/content/find")
    Response<FindNoteContentRspDTO> findNoteContent(@RequestBody FindNoteContentReqDTO findNoteContentReqDTO);

    @PostMapping(value = PREFIX + "/note/content/delete")
    Response<?> deleteNoteContent(@RequestBody DeleteNoteContentReqDTO deleteNoteContentReqDTO);
}