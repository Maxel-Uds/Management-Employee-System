package com.management.employee.system.repositories;

import com.management.employee.system.model.Owner;
import com.management.employee.system.repositories.item.OwnerItem;
import reactor.core.publisher.Mono;

public interface OwnerRepository {

    Mono<Owner> save(OwnerItem owner);
    Mono<Void> delete(String ownerId);
    Mono<Owner> findById(String ownerId);
    Mono<Owner> updateOwner(OwnerItem owner);
    Mono<Owner> findByEmail(String ownerEmail);
}
