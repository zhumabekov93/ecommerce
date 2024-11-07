package com.project.ecommerce.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @Indexed
    private String name;
    private String description;
    private String category;
    private double price;
    private Map<String, Object> attributes;
    private List<String> similarProducts;
    private double averageRating;
    private int viewCount;
    private boolean deleted;
}