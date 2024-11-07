package com.project.ecommerce.resource;

import com.project.ecommerce.model.Product;
import com.project.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String query,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(productService.searchProducts(query, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> editProduct(@RequestBody Product product) {
        String response = productService.editProduct(product) ? "Product updated" : "Product created";
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody Product product) {
        productService.deleteProduct(product.getId());
        return ResponseEntity.ok("Product deleted successfully}");
    }
}