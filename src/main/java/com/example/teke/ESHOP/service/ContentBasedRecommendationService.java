package com.example.teke.ESHOP.service;

import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentBasedRecommendationService {
    private final ProductRepository productRepository;

    public List<Product> getContentBasedRecommendation(Product product){
//          var otherProducts=productRepository.findByCategoryNameOrBrand(product.getCategoryName(), product.getBrand())
        return  productRepository.findByCategoryNameOrBrand(product.getCategoryName(),product.getBrand());
    }

}
