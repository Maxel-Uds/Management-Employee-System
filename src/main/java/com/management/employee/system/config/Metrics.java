package com.management.employee.system.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
@RequiredArgsConstructor
public class Metrics {

    private final MeterRegistry meter;
    private Counter eventProcessedCounter;
    private Counter eventFailedCounter;
    private Counter eventInvalidCounter;

    public static final String COMPANY_CREATION_EVENTS_PROCESSED = "company.creation.events.processed";
    public static final String COMPANY_CREATION_EVENTS_FAILED = "company.creation.events.failed";
    public static final String COMPANY_CREATION_EVENTS_INVALID = "company.creation.events.invalid";

    @PostConstruct
    public void registerCounters() {
        eventProcessedCounter = Counter.builder(COMPANY_CREATION_EVENTS_PROCESSED)
                .description("Quantidade de eventos processados com sucesso")
                .register(meter);

        eventFailedCounter = Counter.builder(COMPANY_CREATION_EVENTS_FAILED)
                .description("Quantidade de eventos que falharam ao serem processados")
                .register(meter);

        eventInvalidCounter = Counter.builder(COMPANY_CREATION_EVENTS_INVALID)
                .description("Quantidade de eventos inválidos")
                .register(meter);

    }

}
