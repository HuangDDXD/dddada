package com.dd.dddada.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * AI生成题目请求
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 题目书
     */
    int questionNumber = 10;

    /**
     * 选项
     */
    int optionNumber = 2;

    public static final long serialVersionUID = 1L;
}
