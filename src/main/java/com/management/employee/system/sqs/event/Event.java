package com.management.employee.system.sqs.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@ToString
@Component
public abstract class Event {

    protected LocalDateTime createdAt = LocalDateTime.now();
}
