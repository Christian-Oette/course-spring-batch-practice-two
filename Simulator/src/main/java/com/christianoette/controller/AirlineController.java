package com.christianoette.controller;

import com.christianoette.controller.dtos.RequestDto;
import com.christianoette.controller.dtos.ResponseDto;
import com.christianoette.services.strategies.AirlineStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AirlineController {

    private final Map<String, AirlineStrategy> airlineStrategies;

    public AirlineController(Map<String, AirlineStrategy> airlineStrategies) {
        this.airlineStrategies = airlineStrategies;
    }

    @GetMapping("/")
    public String index() {
        return "Application up and running";
    }

    @GetMapping("/{airline}/flight-offers")
    public ResponseDto get(@PathVariable("airline") String airline,
                           @RequestBody RequestDto requestDto) {
        return airlineStrategies.get(airline)
                .simulate(requestDto);
    }
}
