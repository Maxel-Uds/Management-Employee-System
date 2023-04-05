package com.management.employee.system.repositories.impl;

import com.management.employee.system.model.AuthUser;
import com.management.employee.system.repositories.AuthUserRepository;
import com.management.employee.system.repositories.item.AuthUserItem;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Repository
public class AuthUserRepositoryImpl implements AuthUserRepository {

    private final DynamoDbAsyncTable<AuthUserItem> table;

    @Autowired
    public AuthUserRepositoryImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        this.table = dynamoDbEnhancedAsyncClient.table(AuthUserItem.TABLE_NAME, TableSchema.fromBean(AuthUserItem.class));
    }

    @Override
    public Mono<AuthUser> save(AuthUserItem authUser) {
        return Mono.fromFuture(table.putItem(authUser)).thenReturn(authUser.toModel());
    }

    @Override
    public Mono<UserDetails> findByUserName(String userName) {
        DynamoDbAsyncIndex<AuthUserItem> responseByName = table.index(AuthUserItem.INDEX_USERNAME);
        SdkPublisher<Page<AuthUserItem>> responseWithName = responseByName.query(r -> r.queryConditional(keyEqualTo(k -> k.partitionValue(userName))));

        return Mono.just(PagePublisher.create(responseWithName).items())
                .flatMapMany(Flux::mergeSequential)
                .map(AuthUserItem::toUserDetails)
                .collectList()
                .flatMap(userDetails -> !userDetails.isEmpty() ?  Mono.just(userDetails.stream().findFirst().orElse(AuthUser.builder().build())) : Mono.empty());
    }

    @Override
    public Mono<AuthUser> updateAuthUserScopes(AuthUser authUser) {
        return Mono.fromFuture(table.updateItem(new AuthUserItem(authUser)))
                .flatMap(authUserItem -> Mono.just(authUserItem.toModel()));
    }

    @Override
    public Mono<Void> delete(String authUserId) {
        log.info("==== Deleting auth user [{}] ====", authUserId);
        return Mono.fromFuture(table.deleteItem(Key.builder().partitionValue(authUserId).build())).then();
    }
}
