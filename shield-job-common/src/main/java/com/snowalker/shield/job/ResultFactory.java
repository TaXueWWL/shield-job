package com.snowalker.shield.job;

import com.snowalker.shield.job.constant.ResultCodeEnum;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/17 15:46
 * @className ResultFactory
 * @desc Result工厂
 */
class ResultFactory<T> {

    /**
     * 返回成功Result
     * @param content
     * @return
     */
    public final Result success(T content) {
        return new Result(true,
                ResultCodeEnum.SUCCESS_CODE.getCode(),
                ResultCodeEnum.SUCCESS_CODE.getDesc(),
                true,
                content);
    }

    /**
     * 返回失败Result
     * @param content
     * @return
     */
    public final Result failure(boolean contentExists, T content) {
        return new Result(false,
                ResultCodeEnum.FAIL_CODE.getCode(),
                ResultCodeEnum.FAIL_CODE.getDesc(),
                contentExists,
                content);
    }

    /**
     * 返回失败Result
     * @param content
     * @return
     */
    public final Result failure(T content) {
        return new Result(false,
                ResultCodeEnum.FAIL_CODE.getCode(),
                ResultCodeEnum.FAIL_CODE.getDesc(),
                content);
    }
}
