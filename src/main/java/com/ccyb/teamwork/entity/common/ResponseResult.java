package com.ccyb.teamwork.entity.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private int code;

    private String msg;

    private T data;

    public static <T> ResponseResult<T> build(int code, String msg, T data) {
        return new ResponseResult<>(code, msg, data);
    }

    public static ResponseResult<?> success() {
        return success(null);
    }

    public static ResponseResult<?> success(String msg) {
        return success(msg, null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return success("success", data);
    }

    public static <T> ResponseResult<T> success(String msg, T data) {
        return build(ResponseCode.SYSTEM_OK, msg, data);
    }

    public static ResponseResult<?> fail() {
        return fail(null);
    }

    public static ResponseResult<?> fail(String msg) {
        return fail(msg, null);
    }

    public static <T> ResponseResult<T> fail(T data) {
        return fail("error", data);
    }

    public static <T> ResponseResult<T> fail(String msg, T data) {
        return build(ResponseCode.SYSTEM_ERROR, msg, data);
    }


    public static <T> ResponseResult<T> databaseSelectSuccess(T object) {
        return build(ResponseCode.DATABASE_SELECT_OK, "success", object);
    }

    public static <T> ResponseResult<T> databaseSaveSuccess(T object) {
        return build(ResponseCode.DATABASE_SAVE_OK, "success", object);
    }

    public static <T> ResponseResult<T> databaseUpdateSuccess(T object) {
        return build(ResponseCode.DATABASE_UPDATE_OK, "success", object);
    }

    public static ResponseResult<?> databaseDeleteSuccess() {
        return build(ResponseCode.DATABASE_DELETE_OK, "success", null);
    }
}
