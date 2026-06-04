package com.hanz.mapper;

import com.hanz.entity.UserAnswer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnswerMapper {
    @Insert("INSERT INTO user_answer (user_id, bank_id, question_id, answer, submit_time) " +
            "VALUES (#{userId}, #{bankId}, #{questionId}, #{answer}, #{submitTime})")
    void insert(UserAnswer answer);

    @Select("SELECT * FROM user_answer WHERE user_id = #{userId} AND bank_id = #{bankId}")
    List<UserAnswer> getUserAnswers(Integer userId, Integer bankId);
}