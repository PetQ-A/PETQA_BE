package com.petqa.apiPayload.apiPayload.exception.handler;

import com.petqa.apiPayload.apiPayload.code.BaseErrorCode;
import com.petqa.apiPayload.apiPayload.exception.GeneralException;

public class FileUploadHandler extends GeneralException {

    public FileUploadHandler(BaseErrorCode code) {
        super(code);
    }
}
