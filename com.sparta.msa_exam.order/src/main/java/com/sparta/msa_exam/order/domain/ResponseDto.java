package com.sparta.msa_exam.order.domain;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {

    public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private Integer code;
    private String msg;
    private T data;

    public ResponseDto(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        data = null;
    }


}
