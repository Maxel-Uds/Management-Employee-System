package com.management.employee.system.repositories.impl;

import com.management.employee.system.repositories.RefreshTokenRepository;
import com.management.employee.system.repositories.item.CompanyItem;
import com.management.employee.system.repositories.item.RefreshTokenItem;
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
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final DynamoDbAsyncTable<RefreshTokenItem> table;

    @Autowired
    public RefreshTokenRepositoryImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        table = dynamoDbEnhancedAsyncClient.table(RefreshTokenItem.TABLE_NAME, TableSchema.fromBean(RefreshTokenItem.class));
    }

    @Override
    public Mono<Void> saveRefreshToken(RefreshTokenItem refreshTokenItem) {
        log.info("==== Saving used refresh token of user [{}] ====", refreshTokenItem.getUsername());
        return Mono.fromFuture(table.putItem(refreshTokenItem));
    }

    @Override
    public Mono<RefreshTokenItem> getRefreshToken(String token) {
        log.info("==== Getting refresh token ====");
        return Mono.fromFuture(table.getItem(Key.builder().partitionValue(token).build()))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("==== Refresh token don't used ====");
                    return Mono.just(new RefreshTokenItem());
                }));
    }
}
