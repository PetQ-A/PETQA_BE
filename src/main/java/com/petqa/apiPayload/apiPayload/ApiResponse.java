package com.petqa.apiPayload.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.petqa.apiPayload.apiPayload.code.BaseCode;
import com.petqa.apiPayload.apiPayload.code.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Standard API response wrapper.
 *
 * @param <T> the type of the response data
 */
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    @JsonProperty("code")
    private final String code;

    @JsonProperty("message")
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    /**
     * Creates a successful ApiResponse with the given data.
     *
     * @param data the data to include in the response
     * @param <T>  the type of the response data
     * @return a new ApiResponse instance indicating success
     */
    public static <T> ApiResponse<T> onSuccess(T data) {
        return new ApiResponse<>(true, SuccessStatus.OK.getCode(), SuccessStatus.OK.getMessage(), data);
    }

    /**
     * Creates an ApiResponse with the given BaseCode and data.
     *
     * @param code the BaseCode representing the status
     * @param data the data to include in the response
     * @param <T>  the type of the response data
     * @return a new ApiResponse instance
     */
    public static <T> ApiResponse<T> of(BaseCode code, T data) {
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), data);
    }

    /**
     * Creates a failure ApiResponse with the given code, message, and data.
     *
     * @param code    the error code
     * @param message the error message
     * @param data    the data to include in the response
     * @param <T>     the type of the response data
     * @return a new ApiResponse instance indicating failure
     */
    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
