package com.example.teke.ESHOP.controller;

import com.example.teke.ESHOP.dto.ProductDTO;
import com.example.teke.ESHOP.exceptions.InsufficientPermissionsException;
import com.example.teke.ESHOP.model.Customer;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.model.UserInteraction;
import com.example.teke.ESHOP.repository.CustomerRepository;
import com.example.teke.ESHOP.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private  final ProductService  productService;


   private final ContentBasedRecommendationService contentBasedRecommendationService;
   private final CollaborativeRecommendationService collaborativeRecommendationService;
   private final CustomerService customerService;
   private final UserInteractionService userInteractionService;
   private final CustomerRepository customerRepository;




    @PostMapping("/addProduct")
    public Product addProduct(
            @RequestParam("price") BigDecimal price,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("brand") String brand,
            @RequestParam("stock") int stock,
            @RequestParam("detail") String detail,
            @RequestParam("barcode") String barcode,
            @RequestParam("imageFile") MultipartFile imageFile) throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setPrice(price);
        productDTO.setCategoryName(categoryName);
        productDTO.setBrand(brand);
        productDTO.setStock(stock);
        productDTO.setDetail(detail);
        productDTO.setBarcode(barcode);
        productDTO.setImageFile(imageFile);

        // Burada service çağrısı yapılıyor.
        return productService.addProduct(productDTO);
    }

    @PutMapping("/updateProduct/{id}")
    public Product updateProduct
            (
                    @RequestParam("price") BigDecimal price,
                     @RequestParam("categoryName") String categoryName,
                   @RequestParam("brand") String brand,
                  @RequestParam("stock") int stock,
                 @RequestParam("detail") String detail,
                @RequestParam("barcode") String barcode,
                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
             @PathVariable("id") UUID id
             ) throws Exception{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new InsufficientPermissionsException("You are not allowed to update this product");
        }


        ProductDTO productDTO = new ProductDTO();
        productDTO.setPrice(price);
        productDTO.setCategoryName(categoryName);
        productDTO.setBrand(brand);
        productDTO.setStock(stock);
        productDTO.setDetail(detail);
        productDTO.setBarcode(barcode);
        productDTO.setImageFile(imageFile);
        return productService.updateProduct(id,productDTO);
    }

    @GetMapping
    public Iterable<Product> getAllProducts (){
        return productService.getAllProducts();
    }
    
    @GetMapping("products/{categoryName}")
    public Iterable<Product> getProductsByCategory(@PathVariable("categoryName") String categoryName) {
        return productService.getProductsByCategory(categoryName);
    }


    @DeleteMapping("/deleteProduct/{barcode}")
    public Product deleteByBarcode(@PathVariable("barcode") String barcode){
        return productService.deleteByBarcode(barcode);
    }

    @PostMapping("/sellProduct")
    public Product sellProduct(String barcode, int count){
        return productService.sellProduct(barcode, count);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCustomerUsername = authentication.getName();
        Customer customer = customerService.getCustomerByUsername(currentCustomerUsername).orElseThrow();
        Product product = productService.getProductById(id);

        // Track the interaction using the service
        userInteractionService.trackInteraction(customer, product);

        return product;
    }



//
//    @GetMapping("/{productId}/recommendations")
//    public List<Product> getRecommendations(@PathVariable UUID productId) {
//        // Fetch the current product by its ID
//        Product product = productService.getProductById(productId);
//
//        // Get recommendations based on category or brand
//        List<Product> recommendations = contentBasedRecommendationService.getContentBasedRecommendation(product);
//
//
//        // Filter out the current product by its ID
//        return recommendations.stream()
//                .filter(recommendedProduct -> !recommendedProduct.getId().equals(productId)) // Exclude the current product
//                .collect(Collectors.toList());
//
//    }
//
//    @GetMapping("/{productId}/collaborative-recommendations")
//    public List<Product> getCollaborativeRecommendations(
//            @PathVariable UUID productId,
//            @RequestParam UUID customerId
//            ) {
//        Product product= productService.getProductById(productId);
//        Customer customer=customerService.getCustomerById(customerId);
//        return collaborativeRecommendationService.getCollaborativeRecommendation(product,customer);
//    }


    @GetMapping("/{productId}/recommendations")
    public List<Product> getCombinedRecommendations(@PathVariable UUID productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCustomerUsername = authentication.getName();

        Customer customer = customerService.getCustomerByUsername(currentCustomerUsername).orElseThrow();
        Product product = productService.getProductById(productId);
        
        List<Product> contentBasedRecommendations = contentBasedRecommendationService.getContentBasedRecommendation(product);
        List<Product> collaborativeRecommendations = collaborativeRecommendationService.getCollaborativeRecommendation(product, customer);

        // Combine both recommendations and remove duplicates
        Set<Product> combinedRecommendations = new HashSet<>(contentBasedRecommendations);
        combinedRecommendations.addAll(collaborativeRecommendations);

        return new ArrayList<>(combinedRecommendations).stream()
                .filter(recommendedProduct -> !recommendedProduct.getId().equals(productId))
                .limit(10)  // Limit to 10 recommendations
                .collect(Collectors.toList());
    }




}
