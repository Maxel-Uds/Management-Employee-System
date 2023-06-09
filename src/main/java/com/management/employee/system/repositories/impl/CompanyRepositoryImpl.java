package com.management.employee.system.repositories.impl;

import com.management.employee.system.model.Company;
import com.management.employee.system.repositories.CompanyRepository;
import com.management.employee.system.repositories.item.CompanyItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.keyEqualTo;

@Slf4j
@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final DynamoDbAsyncTable<CompanyItem> table;

    @Autowired
    public CompanyRepositoryImpl(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        table = dynamoDbEnhancedAsyncClient.table(CompanyItem.TABLE_NAME, TableSchema.fromBean(CompanyItem.class));
    }

    @Override
    public Mono<Company> findByAlias(String alias) {
        log.info("==== Looking for company with alias [{}] ====", alias);
        var item = table.index(CompanyItem.INDEX_ALIAS)
                .query(query -> query.limit(1).queryConditional(keyEqualTo(k -> k.partitionValue(alias))));

        return Mono.just(PagePublisher.create(item).items())
                .flatMapMany(Flux::mergeSequential)
                .map(CompanyItem::toModel)
                .collectList()
                .flatMap(companies -> Mono.just(companies.stream().findFirst().orElse(Company.builder().build())));
    }

    @Override
    public Mono<Company> findByCNPJ(String cnpj) {
        log.info("==== Looking for company with CNPJ [{}] ====", cnpj);
        var item = table.index(CompanyItem.INDEX_DOCUMENT)
                .query(query -> query.limit(1).queryConditional(keyEqualTo(k -> k.partitionValue(cnpj))));

        return Mono.just(PagePublisher.create(item).items())
                .flatMapMany(Flux::mergeSequential)
                .map(CompanyItem::toModel)
                .collectList()
                .flatMap(companies -> Mono.just(companies.stream().findFirst().orElse(Company.builder().build())));
    }

    @Override
    public Mono<Company> save(CompanyItem company) {
        log.info("==== Saving company [{}] ====", company);
        return Mono.fromFuture(table.putItem(company)).thenReturn(company.toModel());
    }

    @Override
    public Mono<Void> delete(String companyId) {
        log.info("==== Deleting company [{}] ====", companyId);
        return Mono.fromFuture(table.deleteItem(Key.builder().partitionValue(companyId).build())).then();
    }

    @Override
    public Mono<Company> findById(String companyId) {
        log.info("==== Looking for company [{}] ====", companyId);
        return Mono.fromFuture(table.getItem(Key.builder().partitionValue(companyId).build()))
                .map(CompanyItem::toModel);
    }

    @Override
    public Mono<Company> updateCompany(CompanyItem companyItem) {
        log.info("===== Updating company [{}] with request [{}] ====", companyItem.getId(), companyItem);
        return Mono.fromFuture(table.updateItem(companyItem)).thenReturn(companyItem.toModel());
    }
}
