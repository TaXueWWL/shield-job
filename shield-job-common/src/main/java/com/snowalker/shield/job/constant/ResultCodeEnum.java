package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/17 15:49
 * @className ResultCodeEnum
 * @desc 结果码枚举
 */
public enum ResultCodeEnum {

    /**成功返回码*/
    SUCCESS_CODE(200, "SUCCESS"),
    /**失败返回码*/
    FAIL_CODE(500, "FAIL");

    private int code;
    private String desc;

    private ResultCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
