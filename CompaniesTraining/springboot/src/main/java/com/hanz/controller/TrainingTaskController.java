package com.hanz.controller;

import com.github.pagehelper.PageInfo;
import com.hanz.common.Result;
import com.hanz.entity.TrainingTask;
import com.hanz.service.TrainingTaskService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trainingTask")
public class TrainingTaskController {
    @Resource
    TrainingTaskService trainingTaskService;

    @PostMapping("/assign")
    public Result assignTask(@RequestBody TrainingTask trainingTask) {
        trainingTaskService.assignTask(trainingTask);
        return Result.success();
    }

    @GetMapping("/getTasksByStaffUserId/{staffUserId}")
    public Result getTasksByStaffUserId(@PathVariable Integer staffUserId) {
        List<TrainingTask> tasks = trainingTaskService.getTasksByStaffUserId(staffUserId);
        return Result.success(tasks);
    }


    // 文件上传目录（建议配置到配置文件中，这里示例为项目根目录下的 uploads 文件夹）
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的图片");
        }

        // 1. 生成文件名（保留扩展名）
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        // 2. 构建完整的文件路径（包含目录）
        String filePath = UPLOAD_DIR + newFileName;
        File destFile = new File(filePath);

        // 3. 检查并创建上传目录
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                return Result.error("无法创建文件上传目录，请检查权限");
            }
        }

        try {
            // 4. 保存文件
            file.transferTo(destFile);
            // 返回文件相对路径（根据前端访问需求调整，例如 "/uploads/xxx.jpg"）
            return Result.success("http://localhost:9999/uploads/" + newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }

    @PostMapping("/submitTask")
    public Result submitTask(@RequestBody TrainingTask task) {
        trainingTaskService.submitTask(task);
        return Result.success();
    }

    @GetMapping("/getTasksForMentor/{mentorId}")
    public Result getTasksForMentor(@PathVariable Integer mentorId) {
        List<TrainingTask> tasks = trainingTaskService.getTasksForMentor(mentorId);
        return Result.success(tasks);
    }


    @PostMapping("/reviewTask")
    public Result reviewTask(@RequestBody TrainingTask task) {
        trainingTaskService.reviewTask(task);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/getTasksPage")
    public Result getTasksPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String trainingPlanTitle,
            @RequestParam(required = false) String staffUserName) {
        PageInfo<TrainingTask> pageInfo = trainingTaskService.getTasksPage(pageNum, pageSize, trainingPlanTitle, staffUserName);
        return Result.success(pageInfo);
    }
}