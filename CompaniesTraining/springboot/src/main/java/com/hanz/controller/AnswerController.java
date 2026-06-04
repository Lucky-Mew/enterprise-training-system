package com.hanz.controller;

import com.hanz.common.Result;
import com.hanz.entity.AnswerDetail;
import com.hanz.entity.Question;
import com.hanz.entity.UserAnswer;
import com.hanz.entity.UserScore;
import com.hanz.mapper.AnswerMapper;
import com.hanz.mapper.QuestionMapper;
import com.hanz.mapper.ScoreMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerMapper answerMapper;
    private final ScoreMapper scoreMapper;
    private final QuestionMapper questionMapper;

    public AnswerController(AnswerMapper answerMapper, ScoreMapper scoreMapper, QuestionMapper questionMapper) {
        this.answerMapper = answerMapper;
        this.scoreMapper = scoreMapper;
        this.questionMapper = questionMapper;
    }


    @GetMapping("/details/{userId}/{bankId}")
    public Result getAnswerDetails(@PathVariable Integer userId, @PathVariable Integer bankId) {
        List<UserAnswer> userAnswers = answerMapper.getUserAnswers(userId, bankId);
        List<AnswerDetail> details = new ArrayList<>();

        for (UserAnswer userAnswer : userAnswers) {
            Question question = questionMapper.selectById(userAnswer.getQuestionId());
            boolean isCorrect = userAnswer.getAnswer().equals(question.getAnswer());
            details
                    .add(new AnswerDetail(
                            question
                                    .getContent(),
                            userAnswer
                                    .getAnswer(),
                            question
                                    .getAnswer(),
                            isCorrect
                    ));
        }

        return Result.success(details);
    }

    @PostMapping("/submit")
    public Result submitAnswers(@RequestBody SubmitRequest request) {
        // 获取当前用户ID（示例用1，实际应从登录信息获取）
        Integer userId = request.getUserId();

        // 1. 计算得分
        int correctCount = 0;
        for (Map.Entry<Integer, String> entry : request.getAnswers().entrySet()) {
            Integer questionId = entry.getKey();
            String userAnswer = entry.getValue();

            // 查询题目正确答案
            Question question = questionMapper.selectById(questionId);
            String correctAnswer = question.getAnswer();

            // 判断答案是否正确（多选题答案需排序后比较）
            if (question.getType().equals("MULTIPLE")) {
                List<String> userAnswers = Arrays.asList(userAnswer.split(","));
                List<String> correctAnswers = Arrays.asList(correctAnswer.split(","));
                Collections.sort(userAnswers);
                Collections.sort(correctAnswers);
                if (userAnswers.equals(correctAnswers)) correctCount++;
            } else {
                if (userAnswer.equals(correctAnswer)) correctCount++;
            }
        }

        // 2. 保存得分记录
        UserScore score = new UserScore();
        score
                .setUserId(userId);
        score
                .setBankId(request.getBankId());
        score
                .setScore(correctCount);
        score
                .setTotal(request.getAnswers().size());
        score
                .setSubmitTime(LocalDateTime.now());
        scoreMapper
                .insert(score);

        // 3. 保存详细答案（原有逻辑）
        request
                .getAnswers().forEach((questionId, answer) -> {
                    UserAnswer userAnswer = new UserAnswer();
                    userAnswer
                            .setUserId(userId);
                    userAnswer
                            .setBankId(request.getBankId());
                    userAnswer
                            .setQuestionId(questionId);
                    userAnswer
                            .setAnswer(answer);
                    userAnswer
                            .setSubmitTime(LocalDateTime.now());
                    answerMapper
                            .insert(userAnswer);
                });

        return Result.success(score); // 返回得分结果
    }

    // 内部请求体定义
    public static class SubmitRequest {
        private Integer userId;
        private Integer bankId;
        private Map<Integer, String> answers; // 使用Map明确类型

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        // Getters and Setters
        public Integer getBankId() {
            return bankId;
        }

        public void setBankId(Integer bankId) {
            this.bankId = bankId;
        }

        public Map<Integer, String> getAnswers() {
            return answers;
        }

        public void setAnswers(Map<Integer, String> answers) {
            this.answers = answers;
        }
    }
}