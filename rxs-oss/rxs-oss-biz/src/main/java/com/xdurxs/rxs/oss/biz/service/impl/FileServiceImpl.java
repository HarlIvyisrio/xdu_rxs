package com.xdurxs.rxs.oss.biz.service.impl;

import com.xdurxs.framework.common.response.Response;
import com.xdurxs.rxs.oss.biz.service.FileService;
import com.xdurxs.rxs.oss.biz.strategy.FileStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private FileStrategy fileStrategy;
    private static final String BUCKET_NAME = "ruixiaosi";
    @Override
    public Response<?> uploadFile(MultipartFile file) {
        String url = fileStrategy.uploadFile(file,BUCKET_NAME);
        return Response.success(url);
    }
}
