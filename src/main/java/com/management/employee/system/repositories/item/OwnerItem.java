package com.management.employee.system.repositories.item;

import com.management.employee.system.controller.request.CompanyOwner;
import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.converter.CustomConverterProvider;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.DefaultAttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.UUID;

@ToString
@DynamoDbBean(converterProviders = {CustomConverterProvider.class, DefaultAttributeConverterProvider.class})
public class OwnerItem {

    public static final String TABLE_NAME = "owner";
    public static final String INDEX_DOCUMENT = "owner_document_index";
    public static final String INDEX_EMAIL = "owner_email_index";

    private String id;
    private String name;
    private String phone;
    private String email;
    private String ownerDocument;
    private String username;

    public OwnerItem() {
    }

    public OwnerItem(CompanyOwner owner, String username) {
        this.id = UUID.randomUUID().toString();
        this.name = owner.getName();
        this.phone = owner.getPhone();
        this.email = owner.getEmail();
        this.ownerDocument = owner.getOwnerDocument();
        this.username = username;
    }

    public OwnerItem(Owner owner) {
        this.id = owner.getId();
        this.name = owner.getName();
        this.phone = owner.getPhone();
        this.email = owner.getEmail();
        this.ownerDocument = owner.getOwnerDocument();
        this.username = owner.getUsername();
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
    public String getOwnerDocument() {
        return ownerDocument;
    }

    public void setOwnerDocument(String document) {
        this.ownerDocument = document;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Owner toModel() {
        return Owner.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .ownerDocument(ownerDocument)
                .email(email)
                .username(username)
                .build();
    }
}
