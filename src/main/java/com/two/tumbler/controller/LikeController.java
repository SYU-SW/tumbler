package com.two.tumbler.controller;

import com.two.tumbler.model.Like;
import com.two.tumbler.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Like> likeProduct(@RequestParam String userId, @RequestParam String productId) {
        Like like = likeService.likeProduct(userId, productId);
        return ResponseEntity.ok(like);
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikeProduct(@RequestParam String userId, @RequestParam String productId) {
        likeService.unlikeProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Like>> getLikesByUser(@RequestParam String userId) {
        List<Like> likes = likeService.getLikesByUser(userId);
        return ResponseEntity.ok(likes);
    }
}
