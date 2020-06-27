package com.christianoette.dontchangeit.controller.dto;

import com.christianoette.dontchangeit.model.Airport;

import java.time.LocalDate;

public class SearchDto {

    public LocalDate flightDate;
    public Airport departureAirport;
    public Airport arrivalAirport;
}
