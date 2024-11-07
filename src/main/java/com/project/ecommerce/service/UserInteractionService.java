package com.project.ecommerce.service;

import com.project.ecommerce.model.UserInteraction;
import com.project.ecommerce.model.enums.InteractionType;
import com.project.ecommerce.repository.UserInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserInteractionService {

    private UserInteractionRepository userInteractionRepository;

    @Autowired
    public UserInteractionService(UserInteractionRepository userInteractionRepository) {
        this.userInteractionRepository = userInteractionRepository;
    }

    @Transactional
    public UserInteraction save(UserInteraction interaction) {
        // Validate interaction
        validateInteraction(interaction);

        // Save and return the interaction
        return userInteractionRepository.save(interaction);
    }

    @Transactional(readOnly = true)
    public List<UserInteraction> findByUserId(String userId, int page, int size) {
        // Convert the pageable request to a subset of the ordered list
        // since we're using the existing findByUserIdOrderByTimestampDesc method
        return userInteractionRepository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<InteractionType, Long> getProductStats(String productId) {
        // Get all interactions for the product
        return userInteractionRepository.findByUserIdOrderByTimestampDesc(productId)
                .stream()
                .collect(Collectors.groupingBy(
                        UserInteraction::getType,
                        Collectors.counting()
                ));
    }


    // Helper method to validate interactions
    private void validateInteraction(UserInteraction interaction) {
        if (interaction.getUserId() == null || interaction.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }

        if (interaction.getProductId() == null || interaction.getProductId().trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty");
        }

        if (interaction.getType() == null) {
            throw new IllegalArgumentException("Interaction type cannot be null");
        }

        // Validate rating if present
        if (interaction.getType() ==InteractionType.RATE) {
            if (interaction.getRating() < 0 || interaction.getRating() > 5) {
                throw new IllegalArgumentException("Rating must be between 0 and 5");
            }
        }
    }
}
