package com.hanz.mapper;

import com.hanz.entity.Question;
import com.hanz.entity.QuestionBank;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface QuestionBankMapper {
    @Insert("INSERT INTO question_bank (title, description) " +
            "VALUES (#{title}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(QuestionBank questionBank);

    @Select("SELECT * FROM question_bank WHERE id = #{id}")
    QuestionBank selectById(Integer id);

    @Select("SELECT * FROM question_bank")
    List<QuestionBank> selectAll();

    // 删除题库主体
    @Delete("DELETE FROM question_bank WHERE id = #{bankId}")
    void deleteById(Integer bankId);

    // 删除关联题目关系
    @Delete("DELETE FROM bank_question WHERE bank_id = #{bankId}")
    void deleteBankQuestions(Integer bankId);

    @Insert("<script>" +
            "INSERT INTO bank_question (bank_id, question_id) VALUES " +
            "<foreach collection='questionIds' item='qid' index='index' separator=','>" +
            " (#{bankId}, #{qid})" +
            "</foreach>" +
            "</script>")
    void insertBankQuestions(@Param("bankId") Integer bankId,
                             @Param("questionIds") List<Integer> questionIds);

    @Select("SELECT q.* FROM question q " +
            "JOIN bank_question bq ON q.id = bq.question_id " +
            "WHERE bq.bank_id = #{bankId}")
    List<Question> selectQuestionsByBankId(Integer bankId);

    @Select("<script>" +
            "SELECT * FROM question_bank " +
            "<if test='keyword != null'>" +
            " WHERE title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')" +
            "</if>" +
            " LIMIT #{pageSize} OFFSET #{offset}" +
            "</script>")
    List<QuestionBank> selectPage(@Param("offset") Integer offset,
                                  @Param("pageSize") Integer pageSize,
                                  @Param("keyword") String keyword);

    @Select("<script>" +
            "SELECT COUNT(*) FROM question_bank " +
            "<if test='keyword != null'>" +
            " WHERE title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')" +
            "</if>" +
            "</script>")
    Integer selectCount(@Param("keyword") String keyword);

}