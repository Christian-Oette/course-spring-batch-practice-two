package com.christianoette.dontchangeit.utils.converters;

import com.christianoette.dontchangeit.model.Airport;
import org.springframework.core.convert.converter.Converter;

public class StringToAirportConverter implements Converter<String, Airport> {

    @Override
    public Airport convert(String source) {
        return Airport.valueOf(source);
    }
}
