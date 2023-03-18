package com.management.employee.system.repositories.item;

import com.management.employee.system.model.Scopes;
import com.management.employee.system.repositories.converter.CustomConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.DefaultAttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Set;

@DynamoDbBean(converterProviders = {CustomConverterProvider.class, DefaultAttributeConverterProvider.class})
public class UserScopeItem {

    public static final String TABLE_NAME = "app_scopes";

    private String userType;
    private Set<String> scopes;


    public UserScopeItem() {
    }

    public UserScopeItem(String userType, Set<String> scopes) {
        this.userType = userType;
        this.scopes = scopes;
    }

    @DynamoDbPartitionKey
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Scopes toModel() {
        return Scopes.builder()
                .scopes(this.scopes)
                .build();
    }
}
