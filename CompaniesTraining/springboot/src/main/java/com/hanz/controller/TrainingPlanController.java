package com.hanz.controller;

import com.github.pagehelper.PageInfo;
import com.hanz.common.Result;
import com.hanz.entity.TrainingPlan;
import com.hanz.service.TrainingPlanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/trainingPlan")
public class TrainingPlanController {

    @Resource
    TrainingPlanService trainingPlanService;

    @PostMapping("/add")
    public Result add(@RequestBody TrainingPlan trainingPlan){ // @RequestBody: 接收前端传来的json的参数
        trainingPlanService.add(trainingPlan);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result del(@PathVariable Integer id){  //@PathVariable 接收前端传来的路径参数
        trainingPlanService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<TrainingPlan> list){  //@RequestBody: 接收前端传来的json的数组
        trainingPlanService.deleteBatch(list);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody TrainingPlan trainingPlan){ // @RequestBody: 接收前端传来的json的参数
        trainingPlanService.update(trainingPlan);
        return Result.success();
    }

    @GetMapping("/selectAll")   //完整的请求路径：http://ip:port/trainingPlan/selectAll
    public Result selectAll(TrainingPlan trainingPlan){
        List<TrainingPlan> trainingPlanList = trainingPlanService.selectAll(trainingPlan);
        return Result.success(trainingPlanList);
    }

    /**
     * 分页查询
     * @param pageNum ：当前的也码
     * @param pageSize：每页展示数据的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             TrainingPlan trainingPlan){
        PageInfo<TrainingPlan> pageInfo = trainingPlanService.selectPage(pageNum, pageSize, trainingPlan);
        return Result.success(pageInfo); // 返回的是分页的对象
    }

}
