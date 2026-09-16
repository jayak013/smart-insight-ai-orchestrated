package com.example.smartinsight.config;

import com.example.smartinsight.model.Product;
import com.example.smartinsight.model.SalesRecord;
import com.example.smartinsight.repository.ProductRepository;
import com.example.smartinsight.repository.SalesRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Configuration
public class DataInitializer {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final Random random = new Random();

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository, SalesRecordRepository salesRecordRepository) {
        return args -> {
            List<Product> products = List.of(
                new Product(null, "Laptop Pro", "Electronics", 1200.0, 15, LocalDateTime.now()),
                new Product(null, "Smartphone X", "Electronics", 800.0, 50, LocalDateTime.now()),
                new Product(null, "Wireless Headphones", "Accessories", 150.0, 100, LocalDateTime.now()),
                new Product(null, "Mechanical Keyboard", "Accessories", 100.0, 5, LocalDateTime.now()),
                new Product(null, "Gaming Mouse", "Accessories", 60.0, 2, LocalDateTime.now()),
                new Product(null, "4K Monitor", "Electronics", 400.0, 20, LocalDateTime.now()),
                new Product(null, "Office Chair", "Furniture", 250.0, 10, LocalDateTime.now()),
                new Product(null, "Desk Lamp", "Furniture", 45.0, 30, LocalDateTime.now())
            );
            
            products = productRepository.saveAll(products);

            // Seed Sales History (Last 30 days)
            for (Product product : products) {
                int salesCount = random.nextInt(10) + 5;
                for (int i = 0; i < salesCount; i++) {
                    int daysAgo = random.nextInt(30);
                    int qty = random.nextInt(3) + 1;
                    salesRecordRepository.save(new SalesRecord(
                            product,
                            qty,
                            product.getPrice(),
                            LocalDateTime.now().minusDays(daysAgo)
                    ));
                }
            }
            
            log.info("Database initialized with {} products and associated sales records.", products.size());
        };
    }
}
