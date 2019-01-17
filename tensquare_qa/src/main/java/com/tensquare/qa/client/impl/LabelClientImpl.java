package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import constants.StatusCode;
import dto.ResultDTO;
import org.springframework.stereotype.Component;

/**
 * 标签操作的本地默认实现
 */
@Component
public class LabelClientImpl implements LabelClient {
    @Override
    public ResultDTO listById(String id) {
        //记录日志、发邮件、发短信等
        System.out.println("调用了熔断器类LabelClientImpl");
        //返回结果
        return new ResultDTO(false, StatusCode.ERROR,"远程调用失败，熔断器启动了");
    }
}
