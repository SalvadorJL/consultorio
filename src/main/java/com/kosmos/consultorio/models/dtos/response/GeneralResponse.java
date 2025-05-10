package com.kosmos.consultorio.models.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GeneralResponse {
    private String message;
    private Integer status;
    private Object data;

    public GeneralResponse(String message, Integer status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

}
