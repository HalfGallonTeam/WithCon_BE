package com.halfgallon.withcon.domain.auth.repository;

import com.halfgallon.withcon.domain.auth.entity.AccessToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, Long> {

  Optional<AccessToken> findByAccessToken(String accessToken);
}
