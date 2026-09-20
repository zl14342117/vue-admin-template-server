package com.demo.admin.controller;

import com.demo.admin.common.ApiResponse;
import com.demo.admin.pojo.dto.SysRoleSaveDTO;
import com.demo.admin.pojo.vo.SysRoleOptionVO;
import com.demo.admin.pojo.vo.SysRolePageVO;
import com.demo.admin.pojo.vo.SysRoleVO;
import com.demo.admin.service.SysRoleService;
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
import java.util.List;

/**
 * 系统角色管理接口，对应前端 src/api/system/role.js。
 */
@RestController
@RequestMapping("/vue-admin-template/system/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /** 分页查询角色列表 */
    @GetMapping("/list")
    public ApiResponse<SysRolePageVO> list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return ApiResponse.success(sysRoleService.list(page, limit, keyword));
    }

    /** 角色下拉选项（用户表单使用） */
    @GetMapping("/options")
    public ApiResponse<List<SysRoleOptionVO>> options() {
        return ApiResponse.success(sysRoleService.options());
    }

    /** 角色详情 */
    @GetMapping("/{id}")
    public ApiResponse<SysRoleVO> detail(@PathVariable("id") Long id) {
        return ApiResponse.success(sysRoleService.detail(id));
    }

    /** 新增角色 */
    @PostMapping
    public ApiResponse<SysRoleVO> create(@RequestBody @Validated SysRoleSaveDTO dto) {
        return ApiResponse.success(sysRoleService.create(dto));
    }

    /** 编辑角色 */
    @PutMapping("/{id}")
    public ApiResponse<SysRoleVO> update(@PathVariable("id") Long id,
                                         @RequestBody @Validated SysRoleSaveDTO dto) {
        return ApiResponse.success(sysRoleService.update(id, dto));
    }

    /** 删除角色 */
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        sysRoleService.delete(id);
        return ApiResponse.success("success");
    }
}
