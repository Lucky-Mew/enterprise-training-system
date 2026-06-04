package com.hanz.controller;

import com.github.pagehelper.PageInfo;
import com.hanz.common.Result;
import com.hanz.entity.Course;
import com.hanz.service.CourseService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    CourseService courseService;

    @PostMapping("/add")
    public Result add(@RequestBody Course course){ // @RequestBody: 接收前端传来的json的参数
        courseService.add(course);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result del(@PathVariable Integer id){  //@PathVariable 接收前端传来的路径参数
        courseService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Course course){ // @RequestBody: 接收前端传来的json的参数
        courseService.update(course);
        return Result.success();
    }

    @GetMapping("/selectAll")   //完整的请求路径：http://ip:port/course/selectAll
    public Result selectAll(Course course){
        List<Course> courseList = courseService.selectAll(course);
        return Result.success(courseList);
    }

    /**
     * 分页查询
     * @param pageNum ：当前的也码
     * @param pageSize：每页展示数据的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Course course){
        PageInfo<Course> pageInfo = courseService.selectPage(pageNum, pageSize, course);
        return Result.success(pageInfo); // 返回的是分页的对象
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Course course = courseService.selectById(id);
        return Result.success(course);
    }

    // 从YAML注入配置值
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/uploadVideo")
    public Result uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件为空");
        }

        try {
            // 生成唯一文件名（保留原始扩展名）
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                    originalFilename
                            .substring(originalFilename.lastIndexOf(".")) : "";
            String safeFileName = UUID.randomUUID() + fileExtension;

            // 创建目录（如果不存在）
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path targetPath = uploadPath.resolve(safeFileName);
            file
                    .transferTo(targetPath);

            // 返回完整 URL
            return Result.success("http://localhost:9999/uploads/" + safeFileName);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

}
