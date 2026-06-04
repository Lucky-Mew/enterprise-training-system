package com.hanz.controller;

import com.hanz.common.Result;
import com.hanz.entity.UserScore;
import com.hanz.mapper.ScoreMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {
    private final ScoreMapper scoreMapper;

    public ScoreController(ScoreMapper scoreMapper) {
        this.scoreMapper = scoreMapper;
    }

    @GetMapping("/user/{userId}")
    public Result getUserScores(@PathVariable Integer userId) {
        List<UserScore> scores = scoreMapper.getUserScores(userId);
        return Result.success(scores);
    }
}