package com.management.employee.system.sqs.listener;

import com.google.gson.Gson;
import com.management.employee.system.config.Metrics;
import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.service.CompanyService;
import com.management.employee.system.sqs.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Component
public class CreateCompanyListener implements SqsListener {

    private final CompanyService companyService;
    private final Integer visibilityTimeout;
    private final Integer maxVisibilityTimeout;
    private final Integer receiveCount;
    private final Integer eventsBatchSize;
    private final Integer eventsWaitTime;
    private final Integer listenerCount;
    private final SqsAsyncClient client;
    private final Metrics metrics;
    private final String url;

    @Autowired
    public CreateCompanyListener(@Value("${aws.sqs.url}") String url,
                                 @Value("${aws.sqs.listeners}") Integer listenerCount,
                                 @Value("${aws.sqs.batch-size}") Integer eventsBatchSize,
                                 @Value("${aws.sqs.wait-time}") Integer eventsWaitTime,
                                 @Value("${aws.sqs.receive-count}") Integer receiveCount,
                                 @Value("${aws.sqs.visibility-timeout}") Integer visibilityTimeout,
                                 @Value("${aws.sqs.max-visibility-timeout}") Integer maxVisibilityTimeout,
                                 Metrics metrics,
                                 SqsAsyncClient client,
                                 CompanyService companyService) {
        this.url = url;
        this.client = client;
        this.listenerCount = listenerCount;
        this.eventsBatchSize = eventsBatchSize;
        this.eventsWaitTime = eventsWaitTime;
        this.receiveCount = receiveCount;
        this.maxVisibilityTimeout = maxVisibilityTimeout;
        this.visibilityTimeout = visibilityTimeout;
        this.metrics = metrics;
        this.companyService = companyService;
    }

    @PostConstruct
    private void listenForever() {
        log.info("Iniciando listener de eventos sqs: {}", url);

        IntStream.range(0, listenerCount).forEach(i -> this.listen()
                .repeat()
                .retry()
                .subscribe());
    }


    @Override
    public Mono<Void> listen() {
        return this.receiveMessage()
                .filter(ReceiveMessageResponse::hasMessages)
                .doOnNext(receivedMessages -> log.info("Processando {} mensagens do SQS", receivedMessages.messages().size()))
                .flatMapIterable(ReceiveMessageResponse::messages)
                .flatMap(this::processEvent)
                .onErrorContinue((ex, o) -> {
                    metrics.getEventFailedCounter().increment();
                    log.error("Não possível processar evento. O evento será ignorado e reprocessado futuramente", ex);
                })
                .then();
    }

    private Mono<ReceiveMessageResponse> receiveMessage() {
        var receiveRequest = ReceiveMessageRequest.builder()
                .maxNumberOfMessages(eventsBatchSize)
                .queueUrl(url)
                .waitTimeSeconds(eventsWaitTime)
                .build();

        return Mono.fromFuture(() -> client.receiveMessage(receiveRequest))
                .doOnError(ex -> log.error("Não foi possível consumir mensagens da fila, sqsUrl " + url, ex));
    }

    private Mono<Void> processEvent(final Message message) {

        if (Objects.nonNull(message.messageId())) {
            int count = getReceiveCount(message);
            var companyCreationRequest = new Gson().fromJson(message.body(), CompanyCreateRequest.class);
            log.info("Evento ID {} sendo processado. Quantidade de vezes processada: {}", message.messageId(), count);

            return this.companyService.createCompany(companyCreationRequest)
                    .doOnError(ex -> handleEventError(message, count, ex))
                    .doOnSuccess(value -> log.info("Evento ID {} processado com sucesso", message.messageId()))
                    .then(Mono.defer(() -> this.deleteMessage(message)));
        }

        return Mono.empty();
    }

    protected int getReceiveCount(final Message message) {
        boolean isValidGetReceivedCount = message.hasAttributes() && message.attributes().containsKey(MessageSystemAttributeName.APPROXIMATE_RECEIVE_COUNT);
        return isValidGetReceivedCount ? Integer.parseInt(message.attributes().get(MessageSystemAttributeName.APPROXIMATE_RECEIVE_COUNT)) : receiveCount;
    }

    protected void handleEventError(final Message message, final int count, final Throwable eventException) {
        int visibilityTimeout = Math.min(this.visibilityTimeout, this.maxVisibilityTimeout);

        client.changeMessageVisibility(ChangeMessageVisibilityRequest.builder()
                        .queueUrl(url)
                        .receiptHandle(message.receiptHandle())
                        .visibilityTimeout(visibilityTimeout)
                        .build())
                .whenComplete((result, sqsException) -> {
                    if (sqsException != null) {
                        log.error("Não foi possível alterar o visibility timeout da mensagem ID {}. sqsUrl: {}, EventException: {}", message.messageId(), url, eventException);
                    }
                    else {
                        log.error("Erro ao processar evento ID {}. Tentativa {}, será processado novamente após {} , sqsUrl: {}", message.messageId(), count, visibilityTimeout, url);
                    }
                });
    }

    private Mono<Void> deleteMessage(final Message message) {
        var deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(url)
                .receiptHandle(message.receiptHandle())
                .build();

        return Mono.fromFuture(client.deleteMessage(deleteRequest))
                .doOnSuccess(value -> {
                    metrics.getEventProcessedCounter().increment();
                    log.info("Mensagem removida da fila com sucesso: ID {}", message.messageId());
                })
                .doOnError(ex -> log.error("Não foi possível remover mensagem da fila, sqsUrl " + url, ex))
                .then(Mono.empty());
    }
}
