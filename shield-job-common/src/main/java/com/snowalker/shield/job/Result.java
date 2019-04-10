package com.snowalker.shield.job;

import com.snowalker.shield.job.constant.ResultCodeEnum;

import java.io.Serializable;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/17 15:38
 * @className Result
 * @desc 结果包装类
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 8315384559109486516L;

    /**返回码*/
    private int code;
    /**返回码描述*/
    private String message;
    /**是否成功*/
    private boolean success;
    /**是否存在返回内容*/
    private boolean contentExists;
    /**返回体*/
    private T content;

    public Result(){}

    public Result(boolean success, int code, String message, boolean contentExists, T content) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.contentExists = contentExists;
        this.content = content;
    }

    public Result(boolean success, int code, String message, T content) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public Result(int code, String message, T content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public Result(T content) {
        this.code = ResultCodeEnum.SUCCESS_CODE.getCode();
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public final Result success(T content) {
        return new ResultFactory().success(content);
    }

    public final Result failure(boolean contentExists, T content) {
        if (content == null && contentExists == true) {
            throw new RuntimeException("if content == null, contentExists must be false!");
        }
        if (content != null && contentExists == false) {
            throw new RuntimeException("if content != null, contentExists must be true!");
        }
        return new ResultFactory().failure(contentExists, content);
    }

    public final Result failure(T content) {
        return new ResultFactory().failure(content);
    }


    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public boolean isContentExists() {
        return contentExists;
    }

    public Result<T> setContentExists(boolean contentExists) {
        this.contentExists = contentExists;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getContent() {
        return content;
    }

    public Result setContent(T content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }
}
