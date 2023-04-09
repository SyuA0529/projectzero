package com.oaoproject.projectzero.repository;

import com.oaoproject.projectzero.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MessageMongoDBRepository extends MongoRepository<Message, String> {
    public Optional<Message> findById(String id);
    public List<Message> findByMessageBodyContaining(String fraction);
    public List<Message> findByMemberId(String memberId);
    public List<Message> findByLatitudeBetweenAndLongitudeBetween(Long latitude1, Long latitude2,
                                                                  Long longitude1, Long longitude2);

    public void deleteByMemberId(String memberId);

}
