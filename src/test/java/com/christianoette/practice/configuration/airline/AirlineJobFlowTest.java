package com.christianoette.practice.configuration.airline;


import com.christianoette.dontchangeit.AirlineConfiguration;
import com.christianoette.dontchangeit.model.Airport;
import com.christianoette.dontchangeit.simulator.SimulatorResponseDto;
import com.christianoette.dontchangeit.utils.CourseUtils;
import com.christianoette.dontchangeit.AirlineSearchService;
import com.christianoette.practice.configuration.JobConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes= {AirlineJobFlowTest.TestConfig.class, JobConfiguration.class})
@EnableBatchProcessing
class AirlineJobFlowTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean(name = Airline.TRANS_AMERICAN_READER)
    private ItemReader<SimulatorResponseDto> transAmericanReader;

    @MockBean(name = Airline.ADIOS_READER)
    private ItemReader<SimulatorResponseDto> adiosAirlineItemReader;

    @MockBean(name = Airline.OCEANIC_AIRLINE_READER)
    private ItemReader<SimulatorResponseDto> oceanicAirlineItemReader;

    @MockBean(name = Airline.BELARUS_AIRLINE_READER)
    private ItemReader<SimulatorResponseDto> belarusAirlineItemReader;

    @MockBean(name = Airline.FLY_US_READER)
    private ItemReader<SimulatorResponseDto> flyUsAirlineItemReader;

    @MockBean(name = Airline.SOUTH_PACIFIC_READER)
    private ItemReader<SimulatorResponseDto> southPacificAirlineItemReader;

    @MockBean(name = AirlineConfiguration.DUBAI_AMSTERDAM_OFFER_TASKLET)
    public Tasklet saveDubaiAmsterdamOffer;

    @MockBean(name = AirlineConfiguration.NEW_YORK_AMSTERDAM_OFFER_TASKLET)
    public Tasklet saveNewYorkAmsterdamOffer;

    @Test
    void thatMultipleAirlinesAreRequested() throws Exception {
        // given
        Airport departureAirport = Airport.PARIS;
        Airport arrivalAirport = Airport.LONDON;
        JobParameters jobParameters = createJobParameters(departureAirport, arrivalAirport);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        // TODO additional assertions here!
    }

    @Test
    void thatDubaiAmsterdamSpecialOfferIsUsed() throws Exception {
        // TODO add test logic here
    }

    @Test
    void thatNewYorkAmsterdamSpecialOfferIsUsed() throws Exception {
        // TODO add test logic here
    }

    @Test
    void thatTimeoutIsSkipped() throws Exception {
        // TODO add test logic here
    }

    @Configuration
    static class TestConfig {

        @Autowired
        private Job airlineSearchJob;

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
            jobLauncherTestUtils.setJob(airlineSearchJob);
            return jobLauncherTestUtils;
        }

    }

    private JobParameters createJobParameters(Airport departureAirport, Airport arrivalAirport) {
        return new JobParametersBuilder()
                .addParameter(AirlineSearchService.JOB_KEY_SEARCH_ID, new JobParameter("-"))
                .addParameter(AirlineSearchService.JOB_KEY_DEPARTURE_DATE, new JobParameter(CourseUtils.toDate(LocalDate.now())))
                .addParameter(AirlineSearchService.JOB_KEY_DEPARTURE_AIRPORT, new JobParameter(departureAirport.name()))
                .addParameter(AirlineSearchService.JOB_KEY_ARRIVAL_AIRPORT, new JobParameter(arrivalAirport.name()))
                .toJobParameters();
    }
}
