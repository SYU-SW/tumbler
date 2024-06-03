package com.two.tumbler.service;

import com.two.tumbler.model.Product;
import com.two.tumbler.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductByName(String name) { return productRepository.findByName(name);}

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(String id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setBrand(productDetails.getBrand());
                    product.setImage(productDetails.getImage());
                    product.setDescription(productDetails.getDescription());
                    product.setSpec(productDetails.getSpec());
                    product.setColor(productDetails.getColor());
                    product.setPrice(productDetails.getPrice());
                    product.setQuantity(productDetails.getQuantity());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public void addStock(String id, int quantity) {
        Product product = getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.addStock(quantity);
        productRepository.save(product);
    }

    public void removeStock(String id, int quantity) {
        Product product = getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.removeStock(quantity);
        productRepository.save(product);
    }
}
