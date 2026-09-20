package com.demo.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.admin.common.ApiCodes;
import com.demo.admin.exception.BusinessException;
import com.demo.admin.mapper.SysRoleMapper;
import com.demo.admin.mapper.SysUserMapper;
import com.demo.admin.pojo.dto.SysRoleSaveDTO;
import com.demo.admin.pojo.entity.SysRoleDO;
import com.demo.admin.pojo.entity.SysUserDO;
import com.demo.admin.pojo.vo.SysRoleOptionVO;
import com.demo.admin.pojo.vo.SysRolePageVO;
import com.demo.admin.pojo.vo.SysRoleVO;
import com.demo.admin.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统角色 MySQL 持久化实现。
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public SysRolePageVO list(Integer page, Integer limit, String keyword) {
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = limit == null || limit < 1 ? 10 : limit;

        LambdaQueryWrapper<SysRoleDO> wrapper = new LambdaQueryWrapper<SysRoleDO>()
                .orderByAsc(SysRoleDO::getId);
        if (StringUtils.hasText(keyword)) {
            String value = keyword.trim();
            wrapper.and(w -> w.like(SysRoleDO::getName, value)
                    .or()
                    .like(SysRoleDO::getCode, value)
                    .or()
                    .like(SysRoleDO::getDescription, value));
        }

        Page<SysRoleDO> result = sysRoleMapper.selectPage(new Page<SysRoleDO>(pageNo, pageSize), wrapper);
        List<SysRoleVO> pageList = result.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new SysRolePageVO(result.getTotal(), pageList);
    }

    @Override
    public List<SysRoleOptionVO> options() {
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRoleDO>()
                        .orderByAsc(SysRoleDO::getId))
                .stream()
                .map(role -> new SysRoleOptionVO(role.getName(), role.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public SysRoleVO detail(Long id) {
        return toVO(findById(id));
    }

    @Override
    public SysRoleVO create(SysRoleSaveDTO dto) {
        if (findByCode(dto.getCode()) != null) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "角色标识已存在");
        }
        SysRoleDO role = new SysRoleDO();
        role.setName(dto.getName().trim());
        role.setCode(dto.getCode().trim());
        role.setDescription(trimToNull(dto.getDescription()));
        role.setCreatedAt(LocalDateTime.now());
        sysRoleMapper.insert(role);
        return toVO(role);
    }

    @Override
    public SysRoleVO update(Long id, SysRoleSaveDTO dto) {
        SysRoleDO role = findById(id);
        if ("admin".equals(role.getCode()) && !"admin".equals(dto.getCode().trim())) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "内置管理员角色标识不可修改");
        }
        SysRoleDO existed = findByCode(dto.getCode());
        if (existed != null && !existed.getId().equals(id)) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "角色标识已存在");
        }
        role.setName(dto.getName().trim());
        role.setCode(dto.getCode().trim());
        role.setDescription(trimToNull(dto.getDescription()));
        sysRoleMapper.updateById(role);
        return toVO(role);
    }

    @Override
    public void delete(Long id) {
        SysRoleDO role = findById(id);
        if ("admin".equals(role.getCode())) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "内置管理员角色不能删除");
        }
        Long used = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUserDO>()
                .eq(SysUserDO::getRole, role.getCode()));
        if (used != null && used > 0) {
            throw new BusinessException(ApiCodes.BAD_REQUEST, "仍有用户使用该角色，无法删除");
        }
        sysRoleMapper.deleteById(id);
    }

    private SysRoleDO findById(Long id) {
        SysRoleDO role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(ApiCodes.NOT_FOUND, "角色不存在");
        }
        return role;
    }

    private SysRoleDO findByCode(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }
        return sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRoleDO>()
                .eq(SysRoleDO::getCode, code.trim())
                .last("LIMIT 1"));
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private SysRoleVO toVO(SysRoleDO role) {
        SysRoleVO vo = new SysRoleVO();
        vo.setId(role.getId());
        vo.setName(role.getName());
        vo.setCode(role.getCode());
        vo.setDescription(role.getDescription());
        if (role.getCreatedAt() != null) {
            vo.setCreatedAt(role.getCreatedAt().format(DATE_TIME_FORMATTER));
        }
        return vo;
    }
}
