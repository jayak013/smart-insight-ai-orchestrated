package com.example.smartinsight.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SalesRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantitySold;
    private Double salePrice;
    private LocalDateTime saleDate;

    public SalesRecord() {}

    public SalesRecord(Product product, Integer quantitySold, Double salePrice, LocalDateTime saleDate) {
        this.product = product;
        this.quantitySold = quantitySold;
        this.salePrice = salePrice;
        this.saleDate = saleDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantitySold() { return quantitySold; }
    public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }
    public Double getSalePrice() { return salePrice; }
    public void setSalePrice(Double salePrice) { this.salePrice = salePrice; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
}
