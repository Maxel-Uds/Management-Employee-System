package com.management.employee.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class SESConfig {

    @Bean
    public SesClient sesClient(AwsCredentialsProvider awsCredentialsProvider, @Value("${aws.region}") String region) {
        return SesClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.of(region))
                .build();
    }
}
