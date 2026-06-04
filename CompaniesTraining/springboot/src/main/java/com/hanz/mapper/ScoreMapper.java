package com.hanz.mapper;

import com.hanz.entity.UserScore;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScoreMapper {
    @Insert("INSERT INTO user_score (user_id, bank_id, score, total, submit_time) " +
            "VALUES (#{userId}, #{bankId}, #{score}, #{total}, #{submitTime})")
    void insert(UserScore score);

    @Select("SELECT * FROM user_score WHERE user_id = #{userId} AND bank_id = #{bankId} ORDER BY submit_time DESC LIMIT 1")
    UserScore getLatestScore(@Param("userId") Integer userId, @Param("bankId") Integer bankId);

    @Select("SELECT * FROM user_score WHERE user_id = #{userId}")
    List<UserScore> getUserScores(Integer userId);
}