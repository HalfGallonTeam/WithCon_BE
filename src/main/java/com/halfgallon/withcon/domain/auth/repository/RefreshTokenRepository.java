package com.halfgallon.withcon.domain.auth.repository;

import com.halfgallon.withcon.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

}
