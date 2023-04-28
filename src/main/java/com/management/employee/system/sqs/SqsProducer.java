package com.management.employee.system.sqs;

import com.management.employee.system.sqs.event.Event;

public interface SqsProducer {

    Event produce(Event event);
}
