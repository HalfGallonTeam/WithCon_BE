package com.halfgallon.withcon.domain.member.repository;

import com.halfgallon.withcon.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByNickname(String nickname);

  boolean existsByPhoneNumber(String phoneNumber);

}
