package com.sparta.msa_exam.product.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private final Integer code;
    private final String msg;
    private final T data;

    public ResponseDto(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        data = null;
    }


}
