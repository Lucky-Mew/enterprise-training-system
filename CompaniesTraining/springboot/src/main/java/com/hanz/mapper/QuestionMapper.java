package com.hanz.mapper;

import com.hanz.entity.Question;
import java.util.List;

public interface QuestionMapper {
    List<Question> selectAll(Question query);

    List<Question> selectTopicAll();

    void insert(Question question);

    void updateById(Question question);

    void deleteById(Integer id);

    Question selectById(Integer id);
}