package com.hanz.mapper;

import com.hanz.entity.StaffUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StaffUserMapper {
    List<StaffUser> selectAll(StaffUser staffUser);

    void insert(StaffUser staffUser);

    StaffUser selectByUsername(String username);

    void updateById(StaffUser staffUser);

    void deleteById(Integer id);

    @Select("select * from `staff_user` where id = #{id}")
    StaffUser selectByid(String id);
}
