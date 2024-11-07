package com.project.ecommerce.model;

import com.project.ecommerce.model.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    @Email
    private String email;
    private String password;
    private String name;
    private Role role;
    private Boolean enabled;
    private List<String> viewHistory = new ArrayList<>();
    private List<String> purchaseHistory= new ArrayList<>();
    private Set<String> preferences = new HashSet<>();
}
