package com.hanz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanz.entity.TrainingPlan;
import com.hanz.mapper.TrainingPlanMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingPlanService {
    @Resource
    TrainingPlanMapper trainingPlanMapper;

    public void add(TrainingPlan trainingPlan) {
        trainingPlanMapper.insert(trainingPlan);
    }

    public void deleteById(Integer id) {
        trainingPlanMapper.deleteById(id);
    }
    //批量删除
    public void deleteBatch(List<TrainingPlan> list) {
        for (TrainingPlan trainingPlan : list) {
            this.deleteById(trainingPlan.getId());
        }
    }

    public TrainingPlan selectById(String id) {
        return trainingPlanMapper.selectByid(id);
    }

    public void update(TrainingPlan trainingPlan) {
        trainingPlanMapper.updateById(trainingPlan);
    }


    public List<TrainingPlan> selectAll(TrainingPlan trainingPlan){
        return trainingPlanMapper.selectAll(trainingPlan);
    }


    public PageInfo<TrainingPlan> selectPage(Integer pageNum, Integer pageSize, TrainingPlan trainingPlan) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<TrainingPlan> list = trainingPlanMapper.selectAll(trainingPlan);
        return PageInfo.of(list);
    }


    public void register(TrainingPlan trainingPlan) {
        this.add(trainingPlan);
    }

}
