package com.two.tumbler.controller;

import com.two.tumbler.model.Cart;
import com.two.tumbler.model.CartItem;
import com.two.tumbler.model.CartItemRequest;
import com.two.tumbler.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody CartItemRequest request) {
        Cart cart = cartService.addToCart(request.getUserId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItem>> getCartItemsByUserId(@PathVariable String userId) {
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@RequestParam String userId, @RequestParam String cartItemId) {
        Cart cart = cartService.removeFromCart(userId, cartItemId);
        return ResponseEntity.ok(cart);
    }
}
