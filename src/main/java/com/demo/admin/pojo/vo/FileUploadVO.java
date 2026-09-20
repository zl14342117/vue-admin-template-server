package com.demo.admin.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传结果。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadVO {

    /** 可访问路径，如 /uploads/xxx.png */
    private String url;
}
