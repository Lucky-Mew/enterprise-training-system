package com.hanz.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanz.entity.Course;
import com.hanz.mapper.CourseMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CourseService {
    @Resource
    CourseMapper courseMapper;

    @Value("${file.upload-dir}")
    private String videoUploadDir;  // 视频存储路径

    @Value("${file.image-dir}")
    private String imageUploadDir;  // 图片存储路径

    public void add(Course course) {
        course.setTime(DateUtil.now());
        courseMapper.insert(course);
    }

    public void deleteById(Integer id) {
        // 1. 查询课程信息获取文件URL
        Course course = courseMapper.selectById(id);
        if (course == null) return;

        // 2. 删除物理文件
        deleteFileByUrl(course.getCoverUrl());
        deleteFileByUrl(course.getVideoUrl());

        // 3. 删除数据库记录
        courseMapper.deleteById(id);
    }

    // 批量删除（增强版）
    public void deleteBatch(List<Course> list) {
        for (Course course : list) {
            this.deleteById(course.getId()); // 复用删除逻辑
        }
    }

    // 通用文件删除方法
    private void deleteFileByUrl(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) return;

        try {
            // 解析文件名
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            // 根据URL路径判断文件类型
            Path filePath;
            if (fileUrl.contains("/files/")) {
                // 图片文件路径：imageUploadDir + 文件名
                filePath = Paths.get(imageUploadDir, fileName);
            } else if (fileUrl.contains("/uploads/")) {
                // 视频文件路径：videoUploadDir + 文件名
                filePath = Paths.get(videoUploadDir, fileName);
            } else {
                System.err.println("未知文件类型URL: " + fileUrl);
                return;
            }

            // 执行删除
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("成功删除文件: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("文件删除失败: " + fileUrl + " | 错误: " + e.getMessage());
        }
    }

    public void update(Course newCourse) {
        // 1. 获取旧数据
        Course oldCourse = courseMapper.selectById(newCourse.getId());
        if (oldCourse == null) return;

        // 2. 比较并删除旧文件
        handleFileUpdate(oldCourse.getCoverUrl(), newCourse.getCoverUrl());
        handleFileUpdate(oldCourse.getVideoUrl(), newCourse.getVideoUrl());

        // 3. 更新数据库
        courseMapper.updateById(newCourse);
    }

    /**
     * 处理文件更新逻辑
     * @param oldUrl 旧文件URL
     * @param newUrl 新文件URL
     */
    private void handleFileUpdate(String oldUrl, String newUrl) {
        // 只有当新旧URL不同且旧URL存在时才删除
        if (StringUtils.hasText(oldUrl) && !oldUrl.equals(newUrl)) {
            deleteFileByUrl(oldUrl);
        }
    }


    public List<Course> selectAll(Course course){
        return courseMapper.selectAll(course);
    }

    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }


    public PageInfo<Course> selectPage(Integer pageNum, Integer pageSize, Course course) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Course> list = courseMapper.selectAll(course);
        return PageInfo.of(list);
    }

}
