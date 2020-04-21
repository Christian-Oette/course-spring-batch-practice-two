package com.christianoette.services.strategies;

import com.christianoette.controller.dtos.FlightDto;
import com.christianoette.controller.dtos.ResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class ResponseGenerator {

    private Random random = new Random();

    public ResponseDto generate(LocalDate requestDate) {
        ResponseDto responseDto = new ResponseDto();
        for (int i = 0; i<5 + randomOfTen(); i++) {
            FlightDto flightDto = new FlightDto();
            LocalDateTime startTime = LocalDateTime.of(requestDate, LocalTime.MIDNIGHT)
                    .plusHours(randomOfTen());
            flightDto.startTime = startTime;
            flightDto.arrivalTime = startTime.plusHours(randomOfTen() + 4);
            flightDto.price = randomPrice();
            responseDto.flights.add(flightDto);
        }
        return responseDto;
    }

    private long randomOfTen() {
        return random.nextInt(10);
    }

    private BigDecimal randomPrice() {
        return new BigDecimal(random.nextInt(400))
                .add( new BigDecimal(String.format("%s%s.%s%s",
                        randomOfTen(), randomOfTen(), randomOfTen(), randomOfTen())));
    }

    public void simulateDelay(int delayInMs) {
        try {
            Thread.sleep(delayInMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
