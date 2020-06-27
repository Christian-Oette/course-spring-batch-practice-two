package com.christianoette.dontchangeit;

import com.christianoette.dontchangeit.model.FlightSearchResultRepository;
import com.christianoette.dontchangeit.simulator.AirlineItemReader;
import com.christianoette.dontchangeit.simulator.AirlineResponseSimulator;
import com.christianoette.dontchangeit.simulator.SimulatorResponseDto;
import com.christianoette.dontchangeit.utils.CourseUtils;
import com.christianoette.practice.configuration.airline.Airline;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

@Configuration
public class AirlineConfiguration {

    private final AirlineResponseSimulator airlineResponseSimulator;
    private final FlightSearchResultRepository flightSearchResultRepository;

    public static final String NEW_YORK_AMSTERDAM_OFFER_TASKLET = "saveNewYorkAmsterdamOffer";
    public static final String DUBAI_AMSTERDAM_OFFER_TASKLET = "saveDubaiAmsterdamOffer";

    @Autowired
    public AirlineConfiguration(AirlineResponseSimulator simulator,
                                FlightSearchResultRepository flightSearchResultRepository) {
        this.airlineResponseSimulator = simulator;
        this.flightSearchResultRepository = flightSearchResultRepository;
    }

    @Bean(name = Airline.ADIOS_READER)
    @StepScope
    public ItemReader<SimulatorResponseDto> adiosAirlineItemReader() {
        return new AirlineItemReader(Airline.ADIOS, airlineResponseSimulator);
    }

    @Bean(name = Airline.FLY_US_READER)
    @StepScope
    public ItemReader<SimulatorResponseDto> flyUsAirlineItemReader() {
        return new AirlineItemReader(Airline.FLY_US, airlineResponseSimulator);
    }

    @Bean(name = Airline.OCEANIC_AIRLINE_READER)
    @StepScope
    public ItemReader<SimulatorResponseDto> oceanicAirlineItemReader() {
        return new AirlineItemReader(Airline.OCEANIC, airlineResponseSimulator);
    }

    @Bean(name = Airline.BELARUS_AIRLINE_READER)
    @StepScope
    public ItemReader<SimulatorResponseDto> belarusAirlineItemReader() {
        return new AirlineItemReader(Airline.BELARUS, airlineResponseSimulator);
    }

    @Bean(name = Airline.SOUTH_PACIFIC_READER)
    @StepScope
    public ItemReader<SimulatorResponseDto> southPacificAirlineItemReader() {
        return new AirlineItemReader(Airline.SOUTH_PACIFIC, airlineResponseSimulator);
    }

    @Bean(name = Airline.TRANS_AMERICAN_READER)
    @StepScope
    public ItemReader<SimulatorResponseDto> transAmericanAirlineItemReader() {
        return new AirlineItemReader(Airline.TRANS_AMERICAN, airlineResponseSimulator);
    }

    @Bean(name = NEW_YORK_AMSTERDAM_OFFER_TASKLET)
    @StepScope
    public Tasklet saveNewYorkAmsterdamOffer() {
        return (contribution, chunkContext) -> specialOfferTasklet(chunkContext, "189.00");
    }

    @Bean(name = DUBAI_AMSTERDAM_OFFER_TASKLET)
    @StepScope
    public Tasklet saveDubaiAmsterdamOffer() {
        return (contribution, chunkContext) -> specialOfferTasklet(chunkContext, "899.00");
    }

    private RepeatStatus specialOfferTasklet(ChunkContext chunkContext, String s) {
        Map<String, Object> jobParameters = chunkContext.getStepContext()
                .getJobParameters();
        String searchId = String.valueOf(jobParameters.get(AirlineSearchService.JOB_KEY_SEARCH_ID));
        LocalDate departureDate = CourseUtils.toLocalDate((Date) jobParameters.get(AirlineSearchService.JOB_KEY_DEPARTURE_DATE));
        LocalDateTime departureDateTime = LocalDateTime.of(departureDate, LocalTime.MIDNIGHT);

        flightSearchResultRepository.saveInternalOffer(new BigDecimal(s),
                departureDateTime,
                departureDateTime.plusHours(4),
                searchId);

        return RepeatStatus.FINISHED;
    }

}
