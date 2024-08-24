package com.dailycodebuffer.orderservice.external.decoder;

import com.dailycodebuffer.orderservice.exception.CustomException;
import com.dailycodebuffer.orderservice.external.responce.ErrorResponce;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("::{}" ,response.request().url());
        log.info("::{}" ,response.request().headers());
        ErrorResponce errorResponce
                = null;
        try {
            errorResponce = objectMapper.readValue(response.body().asInputStream(),
            ErrorResponce.class);
            return new CustomException(errorResponce.getErrorMessage()
                    ,errorResponce.getErrorCode()
                    ,response.status());
        }
        catch (IOException e) {
            throw new CustomException("Internal Server Error"
            ,"INTERNAL_SERVER_ERROR"
                    ,500);
        }
    }
}
