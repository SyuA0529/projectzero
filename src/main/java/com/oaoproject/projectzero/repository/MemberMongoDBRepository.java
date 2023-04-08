package com.oaoproject.projectzero.repository;

import com.oaoproject.projectzero.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberMongoDBRepository extends MongoRepository<Member, String> {
    public Optional<Member> findByname(String memberId);
    public Optional<Member> findByNickname(String nickname);
    public void deleteByNickname(String nickname);
}
