package com.management.employee.system.config;

import com.management.employee.system.sqs.SqsProducer;
import com.management.employee.system.sqs.producer.CreateCompanyProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@RequiredArgsConstructor
public class SQSConfig {

    @Value("${aws.sqs.url}")
    private String sqsUrl;
    @Value("${aws.region}")
    private String region;

    @Bean
    public SqsAsyncClient sqsAsyncClient(AwsCredentialsProvider provider) {
        return SqsAsyncClient
                .builder()
                .region(Region.of(region))
                .credentialsProvider(provider)
                .build();
    }

    @Bean
    public SqsProducer createCompanyProducer(SqsAsyncClient sqsAsyncClient) {
        return new CreateCompanyProducer(sqsUrl, sqsAsyncClient);
    }
}
