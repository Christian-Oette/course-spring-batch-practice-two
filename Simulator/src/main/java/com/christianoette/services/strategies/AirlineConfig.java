package com.christianoette.services.strategies;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@PropertySource("classpath:/simulation.properties")
@ConfigurationProperties(prefix = "airline")
@Validated
@Component
public class AirlineConfig {

        @NotNull
        private Airline transAmerican = new Airline();

        @NotNull
        private Airline southPacific = new Airline();

        @NotNull
        private Airline flyUs = new Airline();

        @NotNull
        private Airline adios = new Airline();

        @NotNull
        private Airline belarus = new Airline();

        @NotNull
        private Airline oceanic = new Airline();

        public class Airline {
                @Min(1)
                private int delay;

                @NotNull
                private boolean throwsError;

                public void setDelay(int delay) {
                        this.delay = delay;
                }

                public int getDelay() {
                        return delay;
                }

                public boolean isThrowsError() {
                        return throwsError;
                }

                public void setThrowsError(String throwsError) {
                        this.throwsError = throwsError.equals("true");
                }
        }

        public Airline getTransAmerican() {
                return transAmerican;
        }

        public void setTransAmerican(Airline transAmerican) {
                this.transAmerican = transAmerican;
        }

        public Airline getSouthPacific() {
                return southPacific;
        }

        public void setSouthPacific(Airline southPacific) {
                this.southPacific = southPacific;
        }

        public Airline getFlyUs() {
                return flyUs;
        }

        public void setFlyUs(Airline flyUs) {
                this.flyUs = flyUs;
        }

        public Airline getAdios() {
                return adios;
        }

        public void setAdios(Airline adios) {
                this.adios = adios;
        }

        public Airline getBelarus() {
                return belarus;
        }

        public void setBelarus(Airline belarus) {
                this.belarus = belarus;
        }

        public Airline getOceanic() {
                return oceanic;
        }

        public void setOceanic(Airline oceanic) {
                this.oceanic = oceanic;
        }
}
