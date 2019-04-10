package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/17 17:03
 * @className ShieldTaskFrameEnum
 * @desc shield-job调度框架类型枚举
 */
public enum ShieldJobSendStatusEnum {

    SEND_STATUS_SUCCESS("SEND_STATUS_SUCCESS", "发送成功"),
    SEND_STATUS_FAIL("SEND_STATUS_FAIL", "发送失败");

    private String code;
    private String desc;

    ShieldJobSendStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
