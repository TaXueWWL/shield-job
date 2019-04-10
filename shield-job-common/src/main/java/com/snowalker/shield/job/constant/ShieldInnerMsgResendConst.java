package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/17 17:09
 * @className CommonConstant
 * @desc 内部消息重发常量定义
 */
public class ShieldInnerMsgResendConst {

    private ShieldInnerMsgResendConst() {}

    /**最大重复投递次数3次，超过三次后不再重发*/
    public static final int MAX_RESEND_TIMES = 3;

    /**
     * 获取内部消息重发次数计数器key
     * @param suffix
     * @return
     */
    public static String getResendTimesRedisKey(String suffix) {
        StringBuilder keyBuilder = new StringBuilder();
        return keyBuilder.append("shield:msg_resend:counter:").append(suffix).toString();
    }


}
