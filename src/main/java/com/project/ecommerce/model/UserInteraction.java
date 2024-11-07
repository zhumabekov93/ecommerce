package com.project.ecommerce.model;

import com.project.ecommerce.model.enums.InteractionType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.LocalDateTime;

@Data
@Document(collection = "interactions")
@CompoundIndex(name = "user_product_idx", def = "{'userId': 1, 'productId': 1}")
public class UserInteraction {
    @Id
    private String id;
    private String userId;
    private String productId;
    private InteractionType type;
    private LocalDateTime timestamp;
    private double rating;
}