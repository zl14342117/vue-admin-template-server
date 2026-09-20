package com.demo.admin.service;

import com.demo.admin.pojo.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件上传。
 */
public interface FileService {

    FileUploadVO uploadImage(MultipartFile file);
}
