package com.dd.dddada.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dd.dddada.model.dto.question.QuestionContentDTO;
import com.dd.dddada.model.entity.App;
import com.dd.dddada.model.entity.Question;
import com.dd.dddada.model.entity.ScoringResult;
import com.dd.dddada.model.entity.UserAnswer;
import com.dd.dddada.model.vo.QuestionVO;
import com.dd.dddada.service.QuestionService;
import com.dd.dddada.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScoringStrategyConfig(appType = 1, scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appId = app.getId();
        // 1.根据id查询题目和题目结果信息
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class)
                        .eq(Question::getAppId, appId));

        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId));
        // 2. 统计用户每个选择对应的属性个数
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
        Map<String, Integer> optionCount = new HashMap<>();

        for (QuestionContentDTO questionContentDTO : questionContent) {

            for (String answer : choices) {
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    if (option.getKey().equals(answer)) {
                        String result = option.getResult();

                        if (!optionCount.containsKey(result)) {
                            optionCount.put(result, 0);
                        }

                        optionCount.put(result, optionCount.get(result) + 1);
                    }
                }
            }
        }
        // 返回最高分数和最高分数对应的评分结果
        // 3. 遍历每种评分结果，计算哪一种结果的得分更高
        ScoringResult maxScoringResult = scoringResultList.get(0);
        int maxScore = 0;
        for (ScoringResult scoringResult : scoringResultList) {
            List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(), String.class);
            int score = resultProp.stream().mapToInt(prop -> optionCount.getOrDefault(prop, 0)).sum();

            if (score > maxScore) {
                maxScore = score;
                maxScoringResult = scoringResult;
            }
        }

        // 4. 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());

        return userAnswer;
    }
}
