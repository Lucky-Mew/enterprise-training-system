package com.hanz.mapper;

import com.hanz.entity.Admin;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminMapper {
    List<Admin> selectAll(Admin admin);

    void insert(Admin admin);

    Admin selectByUsername(String username);

    void updateById(Admin admin);

    void deleteById(Integer id);

    Admin selectById(String id);
}
