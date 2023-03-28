package com.management.employee.system.repositories.item;

import com.management.employee.system.controller.request.CompanyCreateRequest;
import com.management.employee.system.model.Company;
import com.management.employee.system.repositories.converter.CustomConverterProvider;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.DefaultAttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.UUID;

@ToString

@DynamoDbBean(converterProviders = {CustomConverterProvider.class, DefaultAttributeConverterProvider.class})
public class CompanyItem {

    public static final String TABLE_NAME = "company";
    public static final String INDEX_DOCUMENT = "company_document_index";
    public static final String INDEX_ALIAS = "company_alias_index";

    private String id;
    private String name;
    private String alias;
    private String document;
    private String ownerId;

    public CompanyItem() {
    }

    public CompanyItem(CompanyCreateRequest request, String ownerId) {
        this.id = UUID.randomUUID().toString();
        this.name = request.getName();
        this.alias = request.getAlias();
        this.document = request.getDocument();
        this.ownerId = ownerId;
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

    @DynamoDbSecondaryPartitionKey(indexNames = INDEX_ALIAS)
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = INDEX_DOCUMENT)
    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Company toModel() {
        return Company.builder()
                .id(this.id)
                .alias(this.alias)
                .name(this.name)
                .ownerId(this.ownerId)
                .document(this.document)
                .build();
    }
}
