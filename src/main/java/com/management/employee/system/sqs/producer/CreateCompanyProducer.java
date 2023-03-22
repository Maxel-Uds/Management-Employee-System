package com.management.employee.system.sqs.producer;

import com.google.gson.Gson;
import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.sqs.SqsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@RequiredArgsConstructor
public class CreateCompanyProducer implements SqsProducer {

    private final String sqsUrl;
    private final SqsAsyncClient client;

    @Override
    public Mono<CompanyCreateRequest> produce(CompanyCreateRequest request) {
        log.info("==== Sending request to create a company to queue [{}]", sqsUrl);

        client.sendMessage(SendMessageRequest.builder()
                .messageBody(new Gson().toJson(request))
                .queueUrl(sqsUrl)
                .build());

        log.info("==== Request [{}] sended to queue with success ====", request);
        return Mono.just(request);
    }
}
