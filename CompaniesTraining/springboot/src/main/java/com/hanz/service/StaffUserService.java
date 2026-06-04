package com.hanz.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanz.entity.Account;
import com.hanz.entity.Admin;
import com.hanz.entity.StaffUser;
import com.hanz.exception.CustomerException;
import com.hanz.mapper.StaffUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import utils.TokenUtils;

import java.util.List;

@Service
public class StaffUserService {
    @Resource
    StaffUserMapper staffUserMapper;

    public void add(StaffUser staffUser) {
        // 根据新的账号查询数据库，确认是否存在同样账号的数据
        StaffUser dbStaffUser = staffUserMapper.selectByUsername(staffUser.getUsername());
        if (dbStaffUser != null) {
            throw new CustomerException("账号重复");
        }
        // 默认密码
        if (StrUtil.isBlank(staffUser.getPassword())) {
            staffUser.setPassword("123456");
        }
        if (StrUtil.isBlank(staffUser.getName())) {
            staffUser.setName(staffUser.getUsername());
        }
        // 1:管理员; 2:企业导师; 3:企业员工
//        staffUser.setRole(3);
        staffUserMapper.insert(staffUser);
    }

    public void deleteById(Integer id) {
        staffUserMapper.deleteById(id);
    }
    //批量删除
    public void deleteBatch(List<StaffUser> list) {
        for (StaffUser staffUser : list) {
            this.deleteById(staffUser.getId());
        }
    }

    public StaffUser selectById(String id) {
        return staffUserMapper.selectByid(id);
    }

    public void update(StaffUser staffUser) {
        staffUserMapper.updateById(staffUser);
    }


    public List<StaffUser> selectAll(StaffUser staffUser){
        return staffUserMapper.selectAll(staffUser);
    }


    public PageInfo<StaffUser> selectPage(Integer pageNum, Integer pageSize, StaffUser staffUser) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<StaffUser> list = staffUserMapper.selectAll(staffUser);
        return PageInfo.of(list);
    }


    public StaffUser login(Account account) {
        // 验证账号是否存在
        StaffUser dbStaffUser = staffUserMapper.selectByUsername(account.getUsername());
        if (dbStaffUser == null) {
            throw  new CustomerException("账号不存在");
        }
        if (!dbStaffUser.getPassword().equals(account.getPassword())) {
            throw  new CustomerException("账号或密码错误");
        }
        String token = TokenUtils.createToken(dbStaffUser.getId() + "-" + "STAFF_USER", dbStaffUser.getPassword());
        dbStaffUser.setToken(token);
        return dbStaffUser;
    }

    public void register(StaffUser staffUser) {
        this.add(staffUser);
    }


    public void updatePassword(Account account) {
        // 先判断新密码和确认密码是否一致
        if (!account.getNewPassword().equals(account.getNewPassword2())){
            throw new CustomerException("500","您两次输入的密码不一致");
        }
        // 校验一下原密码是否正确
        Account currentUser = TokenUtils.getCurrentUser();
        if (!account.getPassword().equals(currentUser.getPassword())) {
            throw new CustomerException("500", "原密码输入错误");
        }
        //更新密码
        StaffUser staffUser = staffUserMapper.selectByid(currentUser.getId().toString());
        staffUser.setPassword(account.getNewPassword());
        staffUserMapper.updateById(staffUser);
    }
}
