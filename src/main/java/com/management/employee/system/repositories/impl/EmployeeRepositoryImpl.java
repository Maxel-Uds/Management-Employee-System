package com.management.employee.system.repositories.impl;

import com.management.employee.system.model.Employee;
import com.management.employee.system.repositories.EmployeeRepository;
import com.management.employee.system.repositories.item.EmployeeItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Slf4j
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final DynamoDbAsyncTable<EmployeeItem> table;

    @Autowired
    public EmployeeRepositoryImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        this.table = dynamoDbEnhancedAsyncClient.table(EmployeeItem.TABLE_NAME, TableSchema.fromBean(EmployeeItem.class));
    }

    @Override
    public Mono<Employee> save(EmployeeItem employeeItem) {
        log.info("====Saving employee [{}] ====", employeeItem);
        return Mono.fromFuture(table.putItem(employeeItem)).thenReturn(employeeItem.toModel());
    }
}
