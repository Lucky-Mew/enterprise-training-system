package com.hanz.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanz.entity.Notice;
import com.hanz.mapper.NoticeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Resource
    NoticeMapper noticeMapper;

    public void add(Notice notice) {
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }
    //批量删除
    public void deleteBatch(List<Notice> list) {
        for (Notice notice : list) {
            this.deleteById(notice.getId());
        }
    }

    public void update(Notice notice) {
        noticeMapper.updateById(notice);
    }


    public List<Notice> selectAll(Notice notice){
        return noticeMapper.selectAll(notice);
    }


    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize, Notice notice) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }

}
