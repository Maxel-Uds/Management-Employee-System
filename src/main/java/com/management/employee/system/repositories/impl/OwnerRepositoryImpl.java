package com.management.employee.system.repositories.impl;

import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.OwnerRepository;
import com.management.employee.system.repositories.item.OwnerItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Slf4j
@Repository
public class OwnerRepositoryImpl implements OwnerRepository {

    private final DynamoDbAsyncTable<OwnerItem> table;

    @Autowired
    public OwnerRepositoryImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        this.table = dynamoDbEnhancedAsyncClient.table(OwnerItem.TABLE_NAME, TableSchema.fromBean(OwnerItem.class));
    }

    @Override
    public Mono<Owner> save(OwnerItem owner) {
        log.info("==== Saving owner item [{}] ====", owner);
        return Mono.fromFuture(table.putItem(owner)).thenReturn(owner.toModel());
    }

    @Override
    public Mono<Void> delete(String ownerId) {
        return Mono.fromFuture(table.deleteItem(Key.builder().partitionValue(ownerId).build())).then();
    }
}
