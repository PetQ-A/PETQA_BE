package com.petqa.apiPayload.apiPayload.exception.handler;

import com.petqa.apiPayload.apiPayload.code.BaseErrorCode;
import com.petqa.apiPayload.apiPayload.exception.GeneralException;

public class TokenHandler extends GeneralException {

    public TokenHandler(BaseErrorCode code) {
        super(code);
    }
}
