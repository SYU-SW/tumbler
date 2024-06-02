package com.two.tumbler.service;

import com.two.tumbler.model.Review;
import com.two.tumbler.model.User;
import com.two.tumbler.model.Product;
import com.two.tumbler.repository.ReviewRepository;
import com.two.tumbler.repository.UserRepository;
import com.two.tumbler.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Review createReview(String userId, String productId, String content, int rating) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        Review review = Review.builder()
                .user(user)
                .product(product)
                .content(content)
                .rating(rating)
                .date(LocalDateTime.now())
                .build();

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProductId(String productId) {
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getReviewsByUserId(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
