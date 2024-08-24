package com.dailycodebuffer.orderservice.external.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponce {
    private String errorMessage;
    private String errorCode;
}
