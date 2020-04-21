package com.christianoette.controller.dtos;

public class ErrorResponseDto extends GeneralResponseDto{

    public ErrorResponseDto() {
        super.processingStatus = ProcessingStatus.ERROR;
    }
}
