package com.hanz.mapper;

import com.hanz.entity.Course;

import java.util.List;

public interface CourseMapper {
    List<Course> selectAll(Course course);

    void insert(Course course);

    void updateById(Course course);

    void deleteById(Integer id);

    Course selectById(Integer id);
    
}
