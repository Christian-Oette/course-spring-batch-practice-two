package com.christianoette.practice.configuration;

import com.christianoette.dontchangeit.simulator.AirlineSearchResultWriter;
import com.christianoette.dontchangeit.simulator.SimulatorResponseDto;
import com.christianoette.dontchangeit.simulator.airlines.AirlineRequestTimeoutException;
import com.christianoette.dontchangeit.utils.CourseUtilJobSummaryListener;
import com.christianoette.practice.configuration.airline.Airline;
import com.christianoette.practice.configuration.airline.AirlineConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
public class JobConfiguration {

    private static final Logger LOGGER = LogManager.getLogger(JobConfiguration.class);

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApplicationContext applicationContext;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory,
                            StepBuilderFactory stepBuilderFactory,
                            ApplicationContext applicationContext) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.applicationContext = applicationContext;
    }

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("airlineSearchJob")
                .start(searchFlow())
                .end()
                .listener(new CourseUtilJobSummaryListener())
                .build();
    }

    @Bean
    public Flow searchFlow() throws Exception {
        ProviderDecider decider = new ProviderDecider();
        return new FlowBuilder<SimpleFlow>("searchFlow")
                .start(decider)
                .on("INTERNAL_OFFER_ONLY").to(dubaiAmsterdamOffer())
                .from(decider)
                .on("FLIGHT_SEARCH").to(searchAirlinesFlow())
                .from(decider)
                .on("SEARCH_INCLUDING_INTERNAL_OFFER")
                .to(searchIncludingOfferFlow())
                .build();
    }

    private Flow searchIncludingOfferFlow() throws Exception {
        SimpleFlow searchIncludingOfferFlow = new FlowBuilder<SimpleFlow>("searchIncludingOfferFlow")
                .start(newYorkAmsterdamOffer())
                .next(searchAirlinesFlow())
                .build();
        searchIncludingOfferFlow.afterPropertiesSet();
        return searchIncludingOfferFlow;
    }

    @Bean
    public Flow searchAirlinesFlow() throws Exception {
        Flow flow1 = airlineFlow("transAmericanFlow", requestTransamericanStep());
        Flow flow2 = airlineFlow("adiosFlow", requestAdiosStep());
        Flow flow4 = airlineFlow("oceanicFlow", requestOceanicStep());
        Flow flow5 = airlineFlow("BelarusFlow", requestBelarusStep());
        Flow flow3 = airlineFlow("flyUsFlow", requestFlyUsStep());
        Flow flow6 = airlineFlow("soutPacificFlow", requestSouthPacificStep());

        return new FlowBuilder<SimpleFlow>("airlineParallelFlow")
                .split(taskExecutor())
                .add(flow1, flow2,flow3, flow4, flow5, flow6)
                .build();
    }

    private Flow airlineFlow(String flowName, Step step) throws Exception {
        SimpleFlow build = new FlowBuilder<SimpleFlow>(flowName)
                .start(step)
                .build();
        build.afterPropertiesSet();
        return build;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("spring_batch");
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }

    @Bean
    public Step requestOceanicStep() {
        return requestAirlineReaderStep(Airline.OCEANIC);
    }

    @Bean
    public Step requestBelarusStep() {
        return requestAirlineReaderStep(Airline.BELARUS);
    }

    @Bean(value = "requestTransamericanStep")
    public Step requestTransamericanStep() {
        return requestAirlineReaderStep(Airline.TRANS_AMERICAN);
    }

    @Bean
    public Step requestSouthPacificStep() {
        return requestAirlineReaderStep(Airline.SOUTH_PACIFIC);
    }

    @Bean
    public Step requestAdiosStep() {
        return requestAirlineReaderStep(Airline.ADIOS);
    }

    @Bean
    public Step requestFlyUsStep() {
        return requestAirlineReaderStep(Airline.FLY_US);
    }

    @SuppressWarnings("unchecked")
    private Step requestAirlineReaderStep(Airline airline) {
        String beanName = airline.getReaderBeanName();
        ItemReader<SimulatorResponseDto> reader = applicationContext.getBean(beanName, ItemReader.class);
        return stepBuilderFactory.get("request_" + airline)
                .<SimulatorResponseDto, SimulatorResponseDto>chunk(1)
                .reader(reader)
                .faultTolerant()
                .skipLimit(1)
                .skip(AirlineRequestTimeoutException.class)
                .writer(airlineSearchResultWriter())
                .build();
    }

    @Bean
    @StepScope
    public AirlineSearchResultWriter airlineSearchResultWriter() {
        return new AirlineSearchResultWriter();
    }

    @Bean
    public Step newYorkAmsterdamOffer() {
        return stepBuilderFactory.get("newYorkAmsterdamOffer")
                .tasklet(applicationContext.getBean(
                        AirlineConfiguration.NEW_YORK_AMSTERDAM_OFFER_TASKLET, Tasklet.class
                )).build();
    }

    @Bean
    public Step dubaiAmsterdamOffer() {
        return stepBuilderFactory.get("dubaiAmsterdamOffer")
                .tasklet(applicationContext.getBean(
                        AirlineConfiguration.DUBAI_AMSTERDAM_OFFER_TASKLET, Tasklet.class
                )).build();
    }
}
