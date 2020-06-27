package com.christianoette.practice.configuration;

import com.christianoette.dontchangeit.model.Airport;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Set;

public class ProviderDecider implements JobExecutionDecider {

    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String departureAirport = jobExecution.getJobParameters()
                .getString("departureAirport");
        String arrivalAirport = jobExecution.getJobParameters()
                .getString("arrivalAirport");
        Set<String> airports = Set.of(departureAirport, arrivalAirport);

        if (airports.contains(Airport.AMSTERDAM.name()) && airports.contains(Airport.NEWYORK.name())) {
            return new FlowExecutionStatus("SEARCH_INCLUDING_INTERNAL_OFFER");
        }

        if (airports.contains(Airport.AMSTERDAM.name()) && airports.contains(Airport.DUBAI.name())) {
            return new FlowExecutionStatus("INTERNAL_OFFER_ONLY");
        }

        return new FlowExecutionStatus("FLIGHT_SEARCH");
    }
}
