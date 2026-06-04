package com.hanz.controller;

import com.hanz.common.Result;
import com.hanz.entity.Account;
import com.hanz.entity.Admin;
import com.hanz.entity.StaffUser;
import com.hanz.exception.CustomerException;
import com.hanz.service.AdminService;
import com.hanz.service.StaffUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    AdminService adminService;
    @Resource
    StaffUserService staffUserService;

    // 表示这是一个get请求的接口
    @GetMapping("/") // 接口的路径，全局唯一的
    public Result hello() {
        return Result.success("hello");     // 接口的返回值，必须要有
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if (account.getRole().equals("1")) {
            dbAccount = adminService.login(account);
        } else if (account.getRole().equals("2")) {
            dbAccount = staffUserService.login(account);
        } else if (account.getRole().equals("3")) {
            dbAccount = staffUserService.login(account);
        } else {
            throw new CustomerException("非法请求");
        }
        return Result.success(dbAccount);
    }

    @PostMapping("/register")
    public Result register(@RequestBody StaffUser staffUser) {
        staffUserService.register(staffUser);
        return Result.success();
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("1".equals(account.getRole())){
            adminService.updatePassword(account);
        }
        if ("2".equals(account.getRole())){
            staffUserService.updatePassword(account);
        }
        if ("3".equals(account.getRole())){
            staffUserService.updatePassword(account);
        }
        return Result.success();
    }
}
