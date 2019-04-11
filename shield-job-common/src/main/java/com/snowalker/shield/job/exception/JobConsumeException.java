package com.snowalker.shield.job.exception;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 11:14
 * @className JobProduceException
 * @desc 任务消费异常
 */
public class JobConsumeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JobConsumeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobConsumeException(String message) {
        super(message);
    }

}
