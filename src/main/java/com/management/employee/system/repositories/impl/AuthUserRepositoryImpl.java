package com.management.employee.system.repositories.impl;

import com.management.employee.system.model.AuthUser;
import com.management.employee.system.repositories.AuthUserRepository;
import com.management.employee.system.repositories.item.AuthUserItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.keyEqualTo;

@Repository
public class AuthUserRepositoryImpl implements AuthUserRepository {

    private final DynamoDbAsyncTable<AuthUserItem> table;

    @Autowired
    public AuthUserRepositoryImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        this.table = dynamoDbEnhancedAsyncClient.table(AuthUserItem.TABLE_NAME, TableSchema.fromBean(AuthUserItem.class));
    }

    @Override
    public Mono<AuthUser> save() {
        return null;
    }

    @Override
    public Mono<UserDetails> findByUserName(String userName) {
        DynamoDbAsyncIndex<AuthUserItem> responseByName = table.index(AuthUserItem.INDEX_USERNAME);
        SdkPublisher<Page<AuthUserItem>> responseWithName = responseByName.query(r -> r.queryConditional(keyEqualTo(k -> k.partitionValue(userName))));

        return Mono.just(PagePublisher.create(responseWithName).items())
                .flatMapMany(Flux::mergeSequential)
                .map(AuthUserItem::toModel)
                .collectList()
                .flatMap(userDetails -> !userDetails.isEmpty() ?  Mono.just(userDetails.stream().findFirst().orElse(AuthUser.builder().build())) : Mono.empty());
    }
}
