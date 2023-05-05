package com.management.employee.system.repositories.item;

import com.management.employee.system.repositories.converter.CustomConverterProvider;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.DefaultAttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@ToString
@DynamoDbBean(converterProviders = {CustomConverterProvider.class, DefaultAttributeConverterProvider.class})
public class RefreshTokenItem {

    public static final String TABLE_NAME = "used_refresh_token";

    private String token;
    private String username;
    private Long tokenExpiration;

    public RefreshTokenItem() {
    }

    public RefreshTokenItem(String token, Long tokenExpiration, String username) {
        this.token = token;
        this.tokenExpiration = tokenExpiration;
        this.username = username;
    }

    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
