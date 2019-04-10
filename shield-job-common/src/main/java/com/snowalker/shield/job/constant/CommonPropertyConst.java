package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/17 17:09
 * @className CommonConstant
 * @desc 公共常量定义
 */
public abstract class CommonPropertyConst {

    private CommonPropertyConst() {}

    /**全局真定义*/
    public static final Boolean TRUE = true;
    /**全局假定义*/
    public static final Boolean FALSE = false;

    /**告警状态 默认*/
    public static final String ALARM_STATUS_DEFAULT = "DEFAULT";
    /**告警状态 无需告警*/
    public static final String ALARM_STATUS_NONEED = "NONEED";
    /**告警状态 告警成功*/
    public static final String ALARM_STATUS_SUCCESS = "SUCCESS";
    /**告警状态 告警失败*/
    public static final String ALARM_STATUS_FAIL = "FAIL";


}
