package com.christianoette.services.strategies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
public class StrategyFactory {

    @Bean
    public ResponseGenerator responseGenerator() {
        return new ResponseGenerator();
    }

    ;

    @Bean(name = "transAmericanAirline")
    public AirlineStrategy transAmericanAirline(
            AirlineConfig config
    ) {
        return new AirlineStrategy("Trans American Airline",
                config.getTransAmerican(), responseGenerator());
    }

    @Bean(name = "southPacificAirline")
    public AirlineStrategy southPacificAirline(
            AirlineConfig config
    ) {
        return new AirlineStrategy("South Pacific Airline",
                config.getSouthPacific(), responseGenerator());
    }

    @Bean(name = "belarusAirline")
    public AirlineStrategy belarusAirline(
            AirlineConfig config
    ) {
        return new AirlineStrategy("Belarus Airline",
                config.getBelarus(), responseGenerator());
    }

    @Bean(name = "adiosAirline")
    public AirlineStrategy adiosAirline(
            AirlineConfig config
    ) {
        return new AirlineStrategy("Adios Airline",
                config.getAdios(), responseGenerator());
    }

    @Bean(name = "oceanicAirline")
    public AirlineStrategy oceanicAirline(
            AirlineConfig config
    ) {
        return new AirlineStrategy("Oceanic Airline",
                config.getOceanic(), responseGenerator());
    }

    @Bean(name = "flyUsAirline")
    public AirlineStrategy flyUsAirline(
            AirlineConfig config
    ) {
        return new AirlineStrategy("FlyUs",
                config.getTransAmerican(), responseGenerator());
    }

}
