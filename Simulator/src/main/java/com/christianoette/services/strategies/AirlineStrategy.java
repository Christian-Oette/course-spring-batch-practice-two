package com.christianoette.services.strategies;

import com.christianoette.controller.dtos.RequestDto;
import com.christianoette.controller.dtos.ResponseDto;

public class AirlineStrategy {

    private final ResponseGenerator responseGenerator;
    private final String airlineName;
    private final AirlineConfig.Airline config;

    public AirlineStrategy(String airlineName,
                           AirlineConfig.Airline config,
                           ResponseGenerator responseGenerator) {
        this.responseGenerator = responseGenerator;
        this.airlineName = airlineName;
        this.config = config;
    }

    public ResponseDto simulate(RequestDto requestDto) {
        if (config.isThrowsError()) {
            throw new ErrorResponseException();
        }
        ResponseDto response = responseGenerator.generate(requestDto.date);
        responseGenerator.simulateDelay(config.getDelay());
        response.airlineName = airlineName;
        return response;
    }
}
