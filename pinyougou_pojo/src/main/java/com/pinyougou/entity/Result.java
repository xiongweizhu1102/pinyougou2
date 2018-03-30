package com.pinyougou.entity;

import java.io.Serializable;

/**
 * @author bear
 * @version 1.0
 * @description com.pinyougou.manager.controller.sellergoods.service.entity
 * @date 2018/3/23
 */
public class Result implements Serializable {
    private  boolean success;
    private  String message;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
