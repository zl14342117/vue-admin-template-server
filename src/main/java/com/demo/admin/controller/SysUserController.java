package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.dto.SysUserSaveDTO;
import com.demo.admin.pojo.vo.SysUserPageVO;
import com.demo.admin.pojo.vo.SysUserVO;
import com.demo.admin.service.SysUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统用户管理接口，对应前端 src/api/system/user.js。
 */
@RestController
@RequestMapping("/vue-admin-template/system/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    /** 分页查询用户列表 */
    @GetMapping("/list")
    public ApiResponse<SysUserPageVO> list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return ApiResponse.success(sysUserService.list(page, limit, keyword));
    }

    /** 查询用户详情 */
    @GetMapping("/{id}")
    public ApiResponse<SysUserVO> detail(@PathVariable("id") Long id) {
        return ApiResponse.success(sysUserService.detail(id));
    }

    /** 新增用户 */
    @PostMapping
    public ApiResponse<SysUserVO> create(@RequestBody @Validated SysUserSaveDTO dto) {
        return ApiResponse.success(sysUserService.create(dto));
    }

    /** 编辑用户 */
    @PutMapping("/{id}")
    public ApiResponse<SysUserVO> update(@PathVariable("id") Long id,
                                         @RequestBody @Validated SysUserSaveDTO dto) {
        return ApiResponse.success(sysUserService.update(id, dto));
    }

    /** 删除用户 */
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        sysUserService.delete(id);
        return ApiResponse.success("success");
    }
}
