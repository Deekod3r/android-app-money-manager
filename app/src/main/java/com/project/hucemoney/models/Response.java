package com.project.hucemoney.models;

import com.project.hucemoney.common.ResponseCode;
import com.project.hucemoney.common.ResponseMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response<T> {
    private String status;
    private String message;
    private T data;

    public Response() {
        this.status = ResponseCode.FAIL;
        this.message = String.format(ResponseMessage.FAIL, "Thao tác");
    }
}
