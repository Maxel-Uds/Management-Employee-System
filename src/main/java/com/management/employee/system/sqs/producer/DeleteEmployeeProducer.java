package com.management.employee.system.sqs.producer;

import com.google.gson.Gson;
import com.management.employee.system.sqs.SqsProducer;
import com.management.employee.system.sqs.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@RequiredArgsConstructor
public class DeleteEmployeeProducer implements SqsProducer {

    private final String sqsUrl;
    private final SqsAsyncClient client;

    @Override
    public Event produce(Event event) {
        log.info("==== Sending request to create a company to queue [{}]", sqsUrl);

        client.sendMessage(SendMessageRequest.builder()
                .messageBody(new Gson().toJson(event))
                .queueUrl(sqsUrl)
                .build());

        log.info("==== Request [{}] sended to queue with success ====", event);
        return event;
    }
}
