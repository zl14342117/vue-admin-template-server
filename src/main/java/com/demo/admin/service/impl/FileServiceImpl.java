package com.demo.admin.service.impl;

import com.demo.admin.common.ApiCodes;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.pojo.vo.FileUploadVO;
import com.demo.admin.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * 将图片保存到本地 uploads 目录。
 */
@Service
public class FileServiceImpl implements FileService {

    private static final long MAX_SIZE = 2L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXT = new HashSet<String>(Arrays.asList("jpg", "jpeg", "png", "webp"));

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public FileUploadVO uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "请选择图片文件");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "图片大小不能超过 2MB");
        }

        String original = file.getOriginalFilename();
        String ext = extractExt(original);
        if (!ALLOWED_EXT.contains(ext)) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "仅支持 jpg/png/webp 格式");
        }

        String contentType = file.getContentType();
        if (StringUtils.hasText(contentType) && !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "文件必须是图片");
        }

        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(dir);
            String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            Path target = dir.resolve(filename);
            file.transferTo(target.toFile());
            return new FileUploadVO("/uploads/" + filename);
        } catch (IOException ex) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "图片上传失败");
        }
    }

    private String extractExt(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}
