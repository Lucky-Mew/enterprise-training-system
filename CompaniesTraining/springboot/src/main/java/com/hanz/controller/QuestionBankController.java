package com.hanz.controller;

import com.hanz.common.Result;
import com.hanz.entity.QuestionBank;
import com.hanz.mapper.QuestionBankMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/questionBank")
public class QuestionBankController {

    private final QuestionBankMapper questionBankMapper;

    public QuestionBankController(QuestionBankMapper questionBankMapper) {
        this.questionBankMapper = questionBankMapper;
    }

    // 新增：获取单个题库信息
    @GetMapping("/{id}")
    public Result getBankById(@PathVariable Integer id) {
        QuestionBank bank = questionBankMapper.selectById(id);
        return Result.success(bank);
    }

    @PostMapping("/create")
    public Result createBank(@RequestBody QuestionBank bank) {
        questionBankMapper.insert(bank);
        return Result.success(bank.getId());
    }

    @PostMapping("/{bankId}/updateQuestions")
    public Result updateQuestions(@PathVariable Integer bankId,
                                  @RequestBody List<Integer> questionIds) {
        // 1. 删除旧关联
        questionBankMapper
                .deleteBankQuestions(bankId);
        // 2. 插入新关联（如果列表不为空）
        if (!questionIds.isEmpty()) {
            questionBankMapper
                    .insertBankQuestions(bankId, questionIds);
        }
        return Result.success();
    }

    @GetMapping("/list")
    public Result getAllBanks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) { // 新增keyword参数

        Integer offset = (page - 1) * pageSize;
        List<QuestionBank> data = questionBankMapper.selectPage(offset, pageSize, keyword);
        Integer total = questionBankMapper.selectCount(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("records", data);
        result.put("total", total);
        return Result.success(result);
    }

    @GetMapping("/{bankId}/questions")
    public Result getBankQuestions(@PathVariable Integer bankId) {
        return Result.success(questionBankMapper.selectQuestionsByBankId(bankId));
    }

    @DeleteMapping("/delete/{bankId}")
    @Transactional
    public Result deleteBank(@PathVariable Integer bankId) {
        // 1. 删除关联题目关系
        questionBankMapper.deleteBankQuestions(bankId);

        // 2. 删除题库主体
        questionBankMapper.deleteById(bankId);

        return Result.success();
    }
}