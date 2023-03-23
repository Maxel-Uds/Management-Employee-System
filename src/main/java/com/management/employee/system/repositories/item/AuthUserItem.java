package com.management.employee.system.repositories.item;

import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Company;
import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.converter.CustomConverterProvider;
import org.springframework.security.core.userdetails.UserDetails;
import software.amazon.awssdk.enhanced.dynamodb.DefaultAttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.UUID;
import java.util.Set;
import java.util.Map;

@DynamoDbBean(converterProviders = {CustomConverterProvider.class, DefaultAttributeConverterProvider.class})
public class AuthUserItem {

    public static final String TABLE_NAME = "auth_user";
    public static final String INDEX_USERNAME = "auth_user_username_index";

    private String id;
    private AuthUser.UserType userType;
    private String document;
    private String username;
    private String password;
    private Set<String> scopes;
    private Map<String, String> payload;

    public AuthUserItem() {
    }

    public AuthUserItem(Owner owner, Set<String> scopes, String pass, Map<String, String> payload) {
        this.id = UUID.randomUUID().toString();
        this.userType = AuthUser.UserType.ADMIN;
        this.document = owner.getDocument();
        this.username = owner.getUsername();
        this.password = pass;
        this.scopes = scopes;
        this.payload = payload;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthUser.UserType getUserType() {
        return userType;
    }

    public void setUserType(AuthUser.UserType userType) {
        this.userType = userType;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = INDEX_USERNAME)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    public UserDetails toUserDetails() {
        return AuthUser.builder()
                .id(id)
                .userType(userType)
                .document(document)
                .password(password)
                .scopes(scopes)
                .payload(payload)
                .username(username)
                .build();
    }

    public AuthUser toModel() {
        return AuthUser.builder()
                .id(id)
                .userType(userType)
                .document(document)
                .password(password)
                .scopes(scopes)
                .payload(payload)
                .username(username)
                .build();
    }
}
