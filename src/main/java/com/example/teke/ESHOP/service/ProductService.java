package com.example.teke.ESHOP.service;

import com.example.teke.ESHOP.dto.ProductDTO;
import com.example.teke.ESHOP.exceptions.ProductNotFoundException;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.other.ContentBasedRecommender;
import com.example.teke.ESHOP.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.management.RuntimeMBeanException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    ContentBasedRecommender recommender = new ContentBasedRecommender();

    @Autowired
    ProductRepository productRepository;

    public Product addProduct(ProductDTO productDTO) throws Exception {

        /*Product existProduct = (Product) productRepository.findByDetail(productDTO.getDetail());

        if (!ObjectUtils.isEmpty(existProduct)) {
            throw new RuntimeException("Product exist!");
        }

         */

        Product newProduct = new Product();

        UUID id = UUID.randomUUID();
        newProduct.setId(id);
        newProduct.setStock(productDTO.getStock());
        newProduct.setBarcode(productDTO.getBarcode());
        newProduct.setBrand(productDTO.getBrand());
        newProduct.setDetail(productDTO.getDetail());
        newProduct.setPrice(productDTO.getPrice());
        newProduct.setCategoryName(productDTO.getCategoryName());
        MultipartFile imageFile = productDTO.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            newProduct.setImageUrl(imageFile.getBytes());
        }

        Product product = productRepository.save(newProduct);
        return product;
    }

    @Transactional
    public void updateProduct(UUID productId,int quantity) throws Exception {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();

            if (product.getStock()>=quantity){
                int updatedRows=productRepository.updateStock(productId,quantity);
                if (updatedRows == 0){
                    throw new Exception("Could not update stock");
                }
                else {
                    throw new Exception("Insufficient stock");
                }
            }
            else {
                throw new ProductNotFoundException("Product Not Found");
            }
        }

    }
    public Product updateProduct(UUID id,ProductDTO productDTO) throws IOException {
        //Finding a product by it id
        Product product = productRepository.findById(id).orElseThrow(
                ()-> new ProductNotFoundException("Product doesn't exist")
        );


        // Ürün bilgilerini güncelle
        product.setPrice(productDTO.getPrice());
        product.setDetail(productDTO.getDetail());
        product.setStock(productDTO.getStock());
        product.setBrand(productDTO.getBrand());
        product.setCategoryName(productDTO.getCategoryName());

        // Eğer yeni bir resim dosyası varsa onu güncelle
        if (productDTO.getImageFile() != null && !productDTO.getImageFile().isEmpty()) {
            // Resmi byte[] formatına çevirip kaydet
            product.setImageUrl(productDTO.getImageFile().getBytes());
        }

        // Güncellenen ürünü veritabanına kaydet
        return productRepository.save(product);
    }

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Iterable<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }


    public Product getProductById(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new RuntimeException("Product not found with ID: " + id);
        }
    }

    public Product deleteByBarcode(String barcode) {
        Optional<Product> productDb = Optional.ofNullable(this.productRepository.findByBarcode(barcode));

        this.productRepository.delete(productDb.get());
        return null;
    }

    public Product sellProduct(String barcode, int count) {
        Product existProduct = productRepository.findByBarcode(barcode);
        if (!Objects.nonNull(existProduct)) {
            throw new RuntimeException("Product not exist!");
        }
        if (existProduct.getStock() <= count) {
            throw new RuntimeException("Yeterli ürün yok");
        }
        existProduct.setStock(existProduct.getStock() - count);
        Product product = productRepository.save(existProduct);
        return product;
    }

    public List<Product> productRecommender(Product product) throws IOException, ParseException {
        List<Product> similarProducts = recommender.recommendSimilarProducts(product, 3);
        return similarProducts;
    }


    public void updateProductStock(Product product) {

    }
}
