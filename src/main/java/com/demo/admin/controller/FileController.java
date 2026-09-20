package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.vo.FileUploadVO;
import com.demo.admin.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文件上传接口。
 */
@RestController
@RequestMapping("/vue-admin-template/file")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<FileUploadVO> upload(@RequestParam("file") MultipartFile file) {
        return ApiResponse.success(fileService.uploadImage(file));
    }
}
