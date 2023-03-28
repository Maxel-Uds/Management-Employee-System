package com.management.employee.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

@Slf4j
@Configuration
public class AwsCredentialConfig {

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(@Value("${aws.role.arn}") String roleArn, @Value("${app.id}") String appId, @Value("${aws.profile}") String profileName) {
        log.info("==== Assuming role with profile [{}] ====", profileName);
        return StsAssumeRoleCredentialsProvider.builder()
                .stsClient(buildStsClient(profileName))
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

    private StsClient buildStsClient(String profileName) {
        return StsClient
                .builder()
                .credentialsProvider(ProfileCredentialsProvider.create(profileName))
                .region(Region.US_EAST_1)
                .build();
    }
}
