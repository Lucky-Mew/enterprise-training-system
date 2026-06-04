package com.hanz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanz.entity.Question;
import com.hanz.mapper.QuestionMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Resource
    private QuestionMapper questionMapper;

    public PageInfo<Question> selectPage(Integer pageNum, Integer pageSize, Question query) {
        PageHelper.startPage(pageNum, pageSize);
        List<Question> list = questionMapper.selectAll(query);
        return PageInfo.of(list);
    }

    public void add(Question question) {
        questionMapper.insert(question);
    }

    public void update(Question question) {
        questionMapper.updateById(question);
    }

    public Question selectById(Integer id) {
        return questionMapper.selectById(id);
    }

    public void deleteById(Integer id) {
        questionMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        ids.forEach(this::deleteById);
    }

    public List<Question> selectAll() {
        List<Question> questions = questionMapper.selectTopicAll();
        // 确保选项数据不为null
        return questions.stream()
                .map(q -> {
                    if (q.getOptions() == null) q.setOptions("[]");
                    return q;
                })
                .collect(Collectors.toList());
    }
}