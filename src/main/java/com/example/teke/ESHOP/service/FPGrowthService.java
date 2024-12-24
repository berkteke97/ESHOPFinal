package com.example.teke.ESHOP.service;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPGrowth;
import com.example.teke.ESHOP.model.Product;
import com.example.teke.ESHOP.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FPGrowthService {

    @Autowired
    private ProductService productService;

    public List<List<Integer>> runFPGrowth(List<int[]> transactions, double minSupport) throws IOException {
        // Geçici giriş dosyası oluştur
        String inputFilePath = "temp_input.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(inputFilePath))) {
            for (int[] transaction : transactions) {
                String transactionLine = Arrays.stream(transaction)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "));
                writer.println(transactionLine);
            }
        }

        // Geçici çıktı dosyası oluştur
        String outputFilePath = "temp_output.txt";

        // AlgoFPGrowth algoritmasını çalıştır
        AlgoFPGrowth algoFPGrowth = new AlgoFPGrowth();
        algoFPGrowth.runAlgorithm(inputFilePath, outputFilePath, minSupport);
        algoFPGrowth.printStats();

        // Çıktı dosyasından sık öğe kümelerini okuyun
        return readFrequentItemsets(outputFilePath);
    }

    private List<List<Integer>> readFrequentItemsets(String outputFilePath) throws IOException {
        List<List<Integer>> frequentItemsets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(outputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Satırı işle ve öğeleri listeye ekle
                if (!line.trim().isEmpty()) {
                    List<Integer> itemset = Arrays.stream(line.split(" "))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    frequentItemsets.add(itemset);
                }
            }
        }
        return frequentItemsets;
    }

    public List<Product> getRecommendations(Product product, List<int[]> transactions, double minSupport) throws IOException {
        List<List<Integer>> frequentItemsets = runFPGrowth(transactions, minSupport);

        // Ürün önerilerini oluştur
        List<Product> recommendations = new ArrayList<>();
        for (List<Integer> itemset : frequentItemsets) {
            if (itemset.contains(product.getId())) {
                for (Integer itemId : itemset) {
                    if (!itemId.equals(product.getId())) {
                        Product recommendedProduct = productService.getProductById(UUID.fromString(String.valueOf(itemId)));
                        recommendations.add(recommendedProduct);
                    }
                }
            }
        }
        return recommendations;
    }
}
