package com.management.employee.system.config;

import com.management.employee.system.sqs.SqsProducer;
import com.management.employee.system.sqs.producer.CreateCompanyProducer;
import com.management.employee.system.sqs.producer.DeleteEmployeeProducer;
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

    @Value("${aws.sqs.create-company-url}")
    private String createCompanyUrl;

    @Value("${aws.sqs.delete-employee-url}")
    private String deleteEmployeeUrl;
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

    @Bean(name = "createCompanyProducer")
    public SqsProducer createCompanyProducer(SqsAsyncClient sqsAsyncClient) {
        return new CreateCompanyProducer(createCompanyUrl, sqsAsyncClient);
    }

    @Bean(name = "deleteEmployeeProducer")
    public SqsProducer deleteEmployeeProducer(SqsAsyncClient sqsAsyncClient) {
        return new DeleteEmployeeProducer(deleteEmployeeUrl, sqsAsyncClient);
    }
}
