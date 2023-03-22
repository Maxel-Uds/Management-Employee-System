package com.management.employee.system.sqs;

import com.management.employee.system.controller.request.CompanyCreateRequest;
import reactor.core.publisher.Mono;

public interface SqsProducer {

    Mono<CompanyCreateRequest> produce(CompanyCreateRequest request);
}
