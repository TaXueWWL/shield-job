package com.snowalker.shield.job.exception;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 11:14
 * @className JobProduceException
 * @desc 任务生产异常
 */
public class JobProduceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JobProduceException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobProduceException(String message) {
        super(message);
    }

}
