package com.management.employee.system.repositories;

import com.management.employee.system.repositories.item.RefreshTokenItem;
import reactor.core.publisher.Mono;

public interface RefreshTokenRepository {

    Mono<Void> saveRefreshToken(RefreshTokenItem refreshTokenItem);
    Mono<RefreshTokenItem> getRefreshToken(String token);
}
