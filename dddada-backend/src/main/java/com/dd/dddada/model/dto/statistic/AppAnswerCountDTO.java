package com.dd.dddada.model.dto.statistic;

import lombok.Data;

/**
 * app 用户提交答案统计
 */
@Data
public class AppAnswerCountDTO {


    private Long appId;

    /**
     *  用户提交答案数
     */
    private Long answerCount;
}
