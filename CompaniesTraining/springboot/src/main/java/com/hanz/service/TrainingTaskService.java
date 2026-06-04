package com.hanz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanz.entity.TrainingTask;
import com.hanz.mapper.TrainingTaskMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingTaskService {
    @Resource
    TrainingTaskMapper trainingTaskMapper;

    public void assignTask(TrainingTask trainingTask) {
        trainingTaskMapper.insert(trainingTask);
    }

    public List<TrainingTask> getTasksByStaffUserId(Integer staffUserId) {
        return trainingTaskMapper.selectByStaffUserId(staffUserId);
    }

    @Transactional
    public void submitTask(TrainingTask task) {
        // 更新任务状态为待审核（状态值设为 1）
//        task.setStatus(1);
        trainingTaskMapper.updateTask(task);
    }

    public List<TrainingTask> getTasksForMentor(Integer mentorId) {
        return trainingTaskMapper.getTasksForMentor(mentorId);
    }

    public void reviewTask(TrainingTask task) {
        trainingTaskMapper.updateTask(task);
    }

    public PageInfo<TrainingTask> getTasksPage(Integer pageNum, Integer pageSize, String trainingPlanTitle, String staffUserName) {
        PageHelper.startPage(pageNum, pageSize);
        List<TrainingTask> tasks = trainingTaskMapper.selectWithSearch(trainingPlanTitle, staffUserName);
        return new PageInfo<>(tasks);
    }


}