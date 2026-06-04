package com.hanz.mapper;

import com.hanz.entity.TrainingPlan;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TrainingPlanMapper {
    List<TrainingPlan> selectAll(TrainingPlan trainingPlan);

    void insert(TrainingPlan trainingPlan);


    void updateById(TrainingPlan trainingPlan);

    void deleteById(Integer id);

    @Select("select * from `staff_user` where id = #{id}")
    TrainingPlan selectByid(String id);
}
