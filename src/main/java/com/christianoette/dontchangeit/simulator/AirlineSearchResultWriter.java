package com.christianoette.dontchangeit.simulator;

import com.christianoette.dontchangeit.model.FlightSearchResultRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class AirlineSearchResultWriter implements ItemWriter<SimulatorResponseDto> {

    @Value("#{jobParameters['searchId']}")
    private String searchId;

    @Autowired
    private FlightSearchResultRepository flightSearchResultRepository;

    @Override
    public void write(List<? extends SimulatorResponseDto> responseList) throws Exception {
        for (SimulatorResponseDto response : responseList) {
            flightSearchResultRepository.saveResultsToDatabase(response, searchId);
        }
    }
}
