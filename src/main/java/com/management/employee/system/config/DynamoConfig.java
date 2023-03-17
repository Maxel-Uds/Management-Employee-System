package com.management.employee.system.config;

import com.management.employee.system.repositories.AuthUserRepository;
import com.management.employee.system.repositories.impl.AuthUserRepositoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
public class DynamoConfig {

    @Value("${aws.dynamodb.region}")
    private String region;

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient(AwsCredentialsProvider awsCredentialsProvider) {
        return DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(DynamoDbAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(awsCredentialsProvider)
                .build()
        ).build();
    }
}
