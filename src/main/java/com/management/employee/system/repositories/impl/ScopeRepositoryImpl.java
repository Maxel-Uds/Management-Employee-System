package com.management.employee.system.repositories.impl;

import com.management.employee.system.controller.request.ScopeUpdateRequest;
import com.management.employee.system.model.AuthUser;
import com.management.employee.system.model.Scopes;
import com.management.employee.system.repositories.ScopeRepository;
import com.management.employee.system.repositories.item.UserScopeItem;
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
public class ScopeRepositoryImpl implements ScopeRepository {

    private final DynamoDbAsyncTable<UserScopeItem> table;

    @Autowired
    public ScopeRepositoryImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        this.table = dynamoDbEnhancedAsyncClient.table(UserScopeItem.TABLE_NAME, TableSchema.fromBean(UserScopeItem.class));
    }

    @Override
    public Mono<Scopes> addScope(ScopeUpdateRequest request, AuthUser.UserType userType) {
        log.info("==== Add scopes [{}] to [{}]", request.getScopes(),  userType.name());
        return this.findScopesByUserType(userType)
                .flatMap(scopes -> Mono.just(scopes.addScope(request.getScopes())))
                .flatMap(scopes -> Mono.fromFuture(table.updateItem(new UserScopeItem(userType.name(), scopes.getScopes()))).then(Mono.just(scopes)));
    }

    @Override
    public Mono<Scopes> removeScope(ScopeUpdateRequest request, AuthUser.UserType userType) {
        log.info("==== Remove scopes [{}] of [{}]", request.getScopes(),  userType.name());
        return this.findScopesByUserType(userType)
                .flatMap(scopes -> Mono.just(scopes.removeScope(request.getScopes())))
                .flatMap(scopes -> Mono.fromFuture(table.updateItem(new UserScopeItem(userType.name(), scopes.getScopes()))).then(Mono.just(scopes)));
    }


    @Override
    public Mono<Scopes> findScopesByUserType(AuthUser.UserType userType) {
        log.info("==== Looking for scopes  of user type [{}] ====", userType.name());
        return Mono.fromFuture(table.getItem(Key.builder()
                .partitionValue(userType.name())
                .build()))
                .flatMap(userScopeItem -> Mono.just(userScopeItem.toModel()));
    }
}
