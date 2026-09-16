package com.example.smartinsight.controller;

import com.example.smartinsight.model.Product;
import com.example.smartinsight.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "Endpoints for viewing the 'Existing Thing' (Product Data)")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    @Operation(summary = "List All Products", description = "Retrieve the full list of products from the database.")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
