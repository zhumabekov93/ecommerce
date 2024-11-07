package com.project.ecommerce.dto;

import com.project.ecommerce.model.enums.InteractionType;
import lombok.Data;

@Data
public class InteractionDto {
    String productId;
    InteractionType type;
    double rating;
}
