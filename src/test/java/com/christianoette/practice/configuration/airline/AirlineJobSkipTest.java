package com.christianoette.practice.configuration.airline;


import com.christianoette.dontchangeit.model.Airport;
import com.christianoette.dontchangeit.simulator.SimulatorResponseDto;
import com.christianoette.dontchangeit.simulator.airlines.AirlineRequestTimeoutException;
import com.christianoette.dontchangeit.utils.CourseUtils;
import com.christianoette.dontchangeit.AirlineSearchService;
import com.christianoette.practice.configuration.JobConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

@SpringBootTest(classes= {AirlineJobSkipTest.TestConfig.class, JobConfiguration.class})
@EnableBatchProcessing
class AirlineJobSkipTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean(name = "transAmericanReader")
    private ItemReader<SimulatorResponseDto> transAmericanReader;

    @MockBean(name = "adiosAirlineItemReader")
    private ItemReader<SimulatorResponseDto> adiosAirlineItemReader;

    @MockBean(name = "oceanicAirlineItemReader")
    private ItemReader<SimulatorResponseDto> oceanicAirlineItemReader;

    @MockBean(name = "belarusAirlineItemReader")
    private ItemReader<SimulatorResponseDto> belarusAirlineItemReader;

    @MockBean(name = "flyUsAirlineItemReader")
    private ItemReader<SimulatorResponseDto> flyUsAirlineItemReader;

    @MockBean(name = "southPacificAirlineItemReader")
    private ItemReader<SimulatorResponseDto> southPacificAirlineItemReader;

    @MockBean(name = "saveDubaiAmsterdamOffer")
    public Tasklet saveDubaiAmsterdamOffer;

    @MockBean(name = "saveNewYorkAmsterdamOffer")
    public Tasklet saveNewYorkAmsterdamOffer;

    @Autowired
    private Step requestTransamericanStep;

    @Test
    void test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter(AirlineSearchService.JOB_KEY_SEARCH_ID, new JobParameter("-"))
                .addParameter(AirlineSearchService.JOB_KEY_DEPARTURE_DATE, new JobParameter(CourseUtils.toDate(LocalDate.now())))
                .addParameter(AirlineSearchService.JOB_KEY_DEPARTURE_AIRPORT, new JobParameter(Airport.PARIS.name()))
                .addParameter(AirlineSearchService.JOB_KEY_ARRIVAL_AIRPORT, new JobParameter(Airport.LONDON.name()))
                .toJobParameters();
        Mockito.when(transAmericanReader.read())
                .thenThrow(new AirlineRequestTimeoutException())
                .thenReturn(null);

        JobExecution jobExecution = jobLauncherTestUtils.launchStep(requestTransamericanStep.getName(), jobParameters);
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
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
}
