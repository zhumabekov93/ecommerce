package com.project.ecommerce.service;


import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.User;
import com.project.ecommerce.model.enums.EventType;
import com.project.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final EventService eventService;
    private final UserService userService;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<Product> searchProducts(String name, String category) {
        if (category != null && !category.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCaseAndCategoryAndDeletedFalse(name, category);
        }
        return productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name);
    }

    public Product getProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public boolean editProduct(Product product) {
        boolean exists = productRepository.existsByName(product.getName());
        if (exists) {
            productRepository.save(product);
            eventService.createProductEvent(userService.getCurrentUser(), EventType.UPDATE, product);
            return true;
        }else{
            Product newProduct = new Product();
            newProduct.setName(product.getName());
            newProduct.setCategory(product.getCategory());
            newProduct.setPrice(product.getPrice());
            newProduct.setDescription(product.getDescription());
            newProduct.setDeleted(false);
            productRepository.save(newProduct);
            eventService.createProductEvent(userService.getCurrentUser(), EventType.CREATE, product);
            return false;
        }
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        product.setDeleted(true);
        productRepository.save(product);
        eventService.createProductEvent(userService.getCurrentUser(), EventType.DELETE, product);
    }
}