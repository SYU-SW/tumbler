package com.two.tumbler.service;

import com.two.tumbler.model.Cart;
import com.two.tumbler.model.CartItem;
import com.two.tumbler.model.Product;
import com.two.tumbler.model.User;
import com.two.tumbler.repository.CartRepository;
import com.two.tumbler.repository.CartItemRepository;
import com.two.tumbler.repository.ProductRepository;
import com.two.tumbler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Cart getOrCreateCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            cart = new Cart(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    public Cart addToCart(String userId, String productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem(product, quantity);
            cartItemRepository.save(newCartItem);
            cart.addCartItem(newCartItem);
        }

        return cartRepository.save(cart);
    }

    public Cart removeFromCart(String userId, String cartItemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        // 카트에서 카트 아이템을 제거
        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        cartRepository.save(cart);

        // 카트 아이템을 제거
        cartItemRepository.delete(cartItem);

        return cart;
    }

    public List<CartItem> getCartItemsByUserId(String userId) {
        return getOrCreateCart(userId).getCartItems();
    }
}
