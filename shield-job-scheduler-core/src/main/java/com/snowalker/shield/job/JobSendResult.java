package com.snowalker.shield.job;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 17:08
 * @className JobSendResult
 * @desc 发送结果实体
 */
public class JobSendResult {

    /**发送成功的Job列表，返回给调用方处理*/
    private List<BaseJob> sendSuccessJobList;

    /**发送失败的Job列表，返回给调用方处理*/
    private List<BaseJob> sendFailureJobList;

    public List<BaseJob> getSendSuccessJobList() {
        return sendSuccessJobList;
    }

    public JobSendResult setSendSuccessJobList(List<BaseJob> sendSuccessJobList) {
        this.sendSuccessJobList = sendSuccessJobList;
        return this;
    }

    public List<BaseJob> getSendFailureJobList() {
        return sendFailureJobList;
    }

    public JobSendResult setSendFailureJobList(List<BaseJob> sendFailureJobList) {
        this.sendFailureJobList = sendFailureJobList;
        return this;
    }

    @Override
    public String toString() {
        return "JobSendResult{" +
                "sendSuccessJobList=" + sendSuccessJobList +
                ", sendFailureJobList=" + sendFailureJobList +
                '}';
    }
}
