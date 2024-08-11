package com.petqa.apiPayload.apiPayload.exception.handler;

import com.petqa.apiPayload.apiPayload.code.BaseErrorCode;
import com.petqa.apiPayload.apiPayload.exception.GeneralException;

public class CommunityHandler extends GeneralException {
    public CommunityHandler(BaseErrorCode message) {
        super(message);
    }
}
