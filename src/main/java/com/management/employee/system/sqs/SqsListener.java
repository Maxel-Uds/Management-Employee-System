package com.management.employee.system.sqs;

import reactor.core.publisher.Mono;

public interface SqsListener {

    Mono<Void> listen();
}
