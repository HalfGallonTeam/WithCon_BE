package com.halfgallon.withcon.domain.auth.repository;

import com.halfgallon.withcon.domain.auth.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
