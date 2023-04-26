package com.management.employee.system.repositories.item;

import com.management.employee.system.controller.request.EmployeeCreateRequest;
import com.management.employee.system.model.Employee;
import com.management.employee.system.repositories.converter.CustomConverterProvider;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.DefaultAttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.UUID;

@ToString

@DynamoDbBean(converterProviders = {CustomConverterProvider.class, DefaultAttributeConverterProvider.class})
public class EmployeeItem {

    public static final String TABLE_NAME = "employee";
    public static final String INDEX_DOCUMENT = "employee_document_index";
    public static final String INDEX_COMPANY_ID = "employee_companyId_index";

    private String id;
    private String name;
    private String email;
    private String document;
    private String username;
    private String companyId;

    public EmployeeItem() {
    }

    public EmployeeItem(EmployeeCreateRequest request, String username) {
        this.id = UUID.randomUUID().toString();
        this.name = request.getName();
        this.email = request.getEmail();
        this.document = request.getDocument();
        this.username = username;
        this.companyId = request.getCompanyId();
    }

    public EmployeeItem(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.email = employee.getEmail();
        this.document = employee.getDocument();
        this.username = employee.getUsername();
        this.companyId = employee.getCompanyId();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = INDEX_COMPANY_ID)
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Employee toModel() {
        return Employee
                .builder()
                .id(this.id)
                .email(this.email)
                .document(this.document)
                .name(this.name)
                .companyId(this.companyId)
                .username(this.username)
                .build();
    }
}
