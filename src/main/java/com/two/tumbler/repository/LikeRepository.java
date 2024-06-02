package com.two.tumbler.repository;

import com.two.tumbler.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LikeRepository extends MongoRepository<Like, String> {
    List<Like> findByUserId(String userId);
    List<Like> findByProductId(String productId);
    Like findByUserIdAndProductId(String userId, String productId);
}
