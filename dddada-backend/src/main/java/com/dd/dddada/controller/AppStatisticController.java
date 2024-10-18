package com.dd.dddada.controller;

import cn.hutool.core.io.FileUtil;
import com.dd.dddada.common.BaseResponse;
import com.dd.dddada.common.ErrorCode;
import com.dd.dddada.common.ResultUtils;
import com.dd.dddada.constant.FileConstant;
import com.dd.dddada.exception.BusinessException;
import com.dd.dddada.exception.ThrowUtils;
import com.dd.dddada.manager.CosManager;
import com.dd.dddada.mapper.UserAnswerMapper;
import com.dd.dddada.model.dto.file.UploadFileRequest;
import com.dd.dddada.model.dto.statistic.AppAnswerCountDTO;
import com.dd.dddada.model.dto.statistic.AppAnswerResultCountDTO;
import com.dd.dddada.model.entity.User;
import com.dd.dddada.model.enums.FileUploadBizEnum;
import com.dd.dddada.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * app 统计分析接口
*
 */
@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {

    @Resource
    private UserAnswerMapper userAnswerMapper;

    /**
     * 热门应用以及回答统计
     * @return
     */
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAppAnswerCount() {
        return ResultUtils.success(userAnswerMapper.doAppAnswerCount());
    }

    /**
     * 某应用回答结果分布统计
     * @param appId
     * @return
     */
    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userAnswerMapper.doAppAnswerResultCount(appId));
    }
}
