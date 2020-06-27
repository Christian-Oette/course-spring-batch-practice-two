package com.christianoette.dontchangeit.simulator;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class SimulatorResponseDto {
    public List<SimulatorFlightDto> flights = new ArrayList<>();
    public String airlineName;
}
