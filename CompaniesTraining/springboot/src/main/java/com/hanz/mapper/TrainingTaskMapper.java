package com.hanz.mapper;

import com.hanz.entity.TrainingTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrainingTaskMapper {
    void insert(TrainingTask trainingTask);

    void updateTask(TrainingTask task);

    List<TrainingTask> getTasksForMentor(Integer mentorId);

    List<TrainingTask> selectByStaffUserId(Integer staffUserId);

    List<TrainingTask> selectWithSearch(
            @Param("trainingPlanTitle") String trainingPlanTitle,
            @Param("staffUserName") String staffUserName);
}