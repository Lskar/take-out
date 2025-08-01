package com.sky.controller.admin;

import com.sky.exception.FileUploadException;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) throws IOException{

        log.info("文件上传：{}",file);

//        String originalFilename = file.getOriginalFilename();
//        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String objectName = UUID.randomUUID() + suffix;
//        String url = aliOssUtil.upload(file.getBytes(),objectName);
        String url = null;
        try {
            url = aliOssUtil.getFilePath(file);
        } catch (IOException e) {
            log.error("文件上传失败：{}",file);
            throw new FileUploadException("文件上传失败");
        }
        log.info("文件上传成功：{}",url);
        return Result.success(url);
    }

}
