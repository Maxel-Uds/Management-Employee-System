package com.management.employee.system.repositories.item;

import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.converter.CustomConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.DefaultAttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@DynamoDbBean(converterProviders = {CustomConverterProvider.class, DefaultAttributeConverterProvider.class})
public class OwnerItem {

    public static final String TABLE_NAME = "owner";
    public static final String INDEX_DOCUMENT = "owner_document_index";
    public static final String INDEX_EMAIL = "owner_email_index";

    private String id;
    private String name;
    private String phone;
    private String email;
    private String document;

    public OwnerItem() {
    }


    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = INDEX_EMAIL)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = INDEX_DOCUMENT)
    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Owner toModel() {
        return Owner.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .document(document)
                .email(email)
                .build();
    }
}
