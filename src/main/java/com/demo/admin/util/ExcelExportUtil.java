package com.demo.admin.util;

import com.alibaba.excel.EasyExcel;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Excel 导出工具。
 */
public final class ExcelExportUtil {

    private ExcelExportUtil() {
    }

    public static <T> void write(HttpServletResponse response, String filename, Class<T> head, List<T> rows)
            throws IOException {
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encoded);
        EasyExcel.write(response.getOutputStream(), head).sheet("sheet1").doWrite(rows);
    }
}
