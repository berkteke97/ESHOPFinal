package com.example.teke.ESHOP.service;

import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.model.UserInteraction;
import com.example.teke.ESHOP.repository.UserInteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollaborativeRecommendationService {

    // UserInteractionRepository, müşteri etkileşimlerini veritabanından almak için kullanılır.
    private final UserInteractionRepository userInteractionRepository;

    /**
     * Verilen bir ürün ve müşteri için, diğer kullanıcıların etkileşimlerini
     * kullanarak önerilerde bulunur.
     *
     * @param currentProduct   Mevcut ürün (kullanıcının incelediği ürün)
     * @param currentCustomer  Mevcut müşteri (kullanıcı)
     * @return List<Product>   Önerilen ürünlerin listesi
     */
    public List<Product> getCollaborativeRecommendation(Product currentProduct, Customer currentCustomer) {

        // Mevcut ürünle ilgili etkileşimleri veritabanından al
        List<UserInteraction> interactions = userInteractionRepository.findByProduct(currentProduct);

        // Önerilen ürünleri saklamak için bir Set oluştur (dublikasyon önlemek için)
        Set<Product> recommendedProducts = new HashSet<>();

        // Mevcut ürünle etkileşimde bulunan her kullanıcı için döngü
        for (UserInteraction interaction : interactions) {
            // Diğer kullanıcıyı etkileşimden al
            Customer otherUser = interaction.getCustomer();

            // Eğer diğer kullanıcı mevcut kullanıcıdan farklıysa
            if (!otherUser.equals(currentCustomer)) {
                // Diğer kullanıcının etkileşimlerini al
                List<UserInteraction> otherUserInteractions = userInteractionRepository.findByCustomer(otherUser);

                // Diğer kullanıcının etkileşimleri üzerinden döngü
                for (UserInteraction otherUserInteraction : otherUserInteractions) {
                    // Eğer etkileşimdeki ürün mevcut ürünle aynı değilse
                    if (!otherUserInteraction.getProduct().equals(currentProduct)) {
                        // Ürünü önerilenler setine ekle
                        recommendedProducts.add(otherUserInteraction.getProduct());
                    }
                }
            }
        }
        // Önerilen ürünleri liste olarak döndür
        return new ArrayList<>(recommendedProducts);
    }
}
