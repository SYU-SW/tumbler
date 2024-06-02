package com.two.tumbler.service;

import com.two.tumbler.model.Like;
import com.two.tumbler.model.Product;
import com.two.tumbler.model.User;
import com.two.tumbler.repository.LikeRepository;
import com.two.tumbler.repository.ProductRepository;
import com.two.tumbler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Like likeProduct(String userId, String productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        Like existingLike = likeRepository.findByUserIdAndProductId(userId, productId);
        if (existingLike != null) {
            throw new IllegalArgumentException("Product already liked by user");
        }

        Like like = new Like(user, product);
        return likeRepository.save(like);
    }

    public void unlikeProduct(String userId, String productId) {
        Like like = likeRepository.findByUserIdAndProductId(userId, productId);
        if (like != null) {
            likeRepository.delete(like);
        }
    }

    public List<Like> getLikesByUser(String userId) {
        return likeRepository.findByUserId(userId);
    }
}
