package com.hanz.controller;

import com.github.pagehelper.PageInfo;
import com.hanz.common.Result;
import com.hanz.entity.Notice;
import com.hanz.service.NoticeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    NoticeService noticeService;

    @PostMapping("/add")
    public Result add(@RequestBody Notice notice){ // @RequestBody: 接收前端传来的json的参数
        noticeService.add(notice);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result del(@PathVariable Integer id){  //@PathVariable 接收前端传来的路径参数
        noticeService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Notice notice){ // @RequestBody: 接收前端传来的json的参数
        noticeService.update(notice);
        return Result.success();
    }

    @GetMapping("/selectAll")   //完整的请求路径：http://ip:port/notice/selectAll
    public Result selectAll(Notice notice){
        List<Notice> noticeList = noticeService.selectAll(notice);
        return Result.success(noticeList);
    }

    /**
     * 分页查询
     * @param pageNum ：当前的也码
     * @param pageSize：每页展示数据的个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Notice notice){
        PageInfo<Notice> pageInfo = noticeService.selectPage(pageNum, pageSize, notice);
        return Result.success(pageInfo); // 返回的是分页的对象
    }

}
