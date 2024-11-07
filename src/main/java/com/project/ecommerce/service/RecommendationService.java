package com.project.ecommerce.service;

import com.project.ecommerce.model.Product;
import com.project.ecommerce.model.UserInteraction;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserInteractionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RecommendationService {
    private final UserInteractionRepository interactionRepository;
    private final ProductRepository productRepository;


    public RecommendationService(UserInteractionRepository interactionRepository, ProductRepository productRepository) {
        this.interactionRepository = interactionRepository;
        this.productRepository = productRepository;
    }

    public List<Product> getPersonalizedRecommendations(String userId, int limit) {
        //Getting user interaction history
        List<UserInteraction> userInteractions = interactionRepository.findByUserId(userId);

        //Getting products and their categories
        Set<String> interactedCategories = new HashSet<>();
        Set<String> interactedProductIds = new HashSet<>();

        for (UserInteraction interaction : userInteractions) {
            Product product = productRepository.findById(interaction.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            interactedCategories.add(product.getCategory());
            interactedProductIds.add(product.getId());
        }

        //Recommendation from each category
        List<Product> recommendations = new ArrayList<>();
        for (String category : interactedCategories) {
            List<Product> categoryRecommendations = productRepository
                    .findByCategoryAndDeletedFalseAndIdNotIn(category, new ArrayList<>(interactedProductIds))
                    .stream()
                    .limit(limit / interactedCategories.size())
                    .toList();
            recommendations.addAll(categoryRecommendations);
        }

        // Shuffle recommendations to add variety
        Collections.shuffle(recommendations);
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }

}