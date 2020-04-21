package com.christianoette.controller.dtos;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ResponseDto extends GeneralResponseDto {

    public List<FlightDto> flights = new ArrayList<>();

    public ResponseDto() {
        super.processingStatus = ProcessingStatus.COMPLETED;
    }

    public String airlineName;
}
