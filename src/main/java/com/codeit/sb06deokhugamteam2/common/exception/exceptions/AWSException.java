package com.codeit.sb06deokhugamteam2.common.exception.exceptions;

import com.codeit.sb06deokhugamteam2.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class AWSException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public AWSException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        details = new HashMap<>();
    }
}
