package com.management.employee.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

@Configuration
public class AwsCredentialConfig {

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(@Value("${aws.role.arn}") String roleArn, @Value("${app.id}") String appId) {
        return StsAssumeRoleCredentialsProvider.builder()
                .stsClient(buildStsClient())
                .refreshRequest(() -> {
                    return AssumeRoleRequest.builder()
                            .roleArn(roleArn)
                            .externalId(appId)
                            .durationSeconds(3600)
                            .roleSessionName("management-employee-system")
                            .build();
                })
                .build();
    }

    private StsClient buildStsClient() {
        return StsClient
                .builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
