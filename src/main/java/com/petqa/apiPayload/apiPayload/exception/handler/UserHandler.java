package com.petqa.apiPayload.apiPayload.exception.handler;


import com.petqa.apiPayload.apiPayload.code.BaseErrorCode;
import com.petqa.apiPayload.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode message) {
        super(message);
    }
}
