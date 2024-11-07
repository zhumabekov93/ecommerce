package com.project.ecommerce.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "events")
public class Event {
    private String userId;
    private Date date;
    private Product product;
    private String eventType;
}
