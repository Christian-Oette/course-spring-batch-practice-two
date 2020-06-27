package com.christianoette.dontchangeit.controller;

import com.christianoette.dontchangeit.controller.dto.FlightOfferDto;
import com.christianoette.dontchangeit.controller.dto.SearchDto;
import com.christianoette.dontchangeit.model.FlightSearchResultRepository;
import com.christianoette.dontchangeit.AirlineSearchService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/holiday")
public class HolidayScanController {

    private final AirlineSearchService airlineSearchService;
    private final FlightSearchResultRepository flightSearchResultRepository;

    public HolidayScanController(AirlineSearchService airlineSearchService, FlightSearchResultRepository flightSearchResultRepository) {
        this.airlineSearchService = airlineSearchService;
        this.flightSearchResultRepository = flightSearchResultRepository;
    }

    @RequestMapping(value = "/search", method =  { RequestMethod.GET, RequestMethod.POST})
    public List<FlightOfferDto> getOffers(@RequestBody @Validated SearchDto searchDto) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        String searchId = UUID.randomUUID().toString();
        airlineSearchService.search(searchId, searchDto.flightDate, searchDto.departureAirport, searchDto.arrivalAirport);
        return flightSearchResultRepository.findResults(searchId);
    }
}
