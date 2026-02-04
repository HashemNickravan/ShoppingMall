package com.shoppingmall.service;

import com.shoppingmall.model.Category;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String keyword, Category category) {
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream()
                .filter(product -> {
                    boolean matchesKeyword = keyword == null || keyword.isEmpty() ||
                            product.getName().toLowerCase().contains(keyword.toLowerCase());
                    boolean matchesCategory = category == null || product.getCategory() == category;
                    return matchesKeyword && matchesCategory;
                })
                .collect(Collectors.toList());
    }

    public List<Product> sortProducts(List<Product> products, String sortBy) {
        List<Product> sorted = new ArrayList<>(products);

        switch (sortBy.toLowerCase()) {
            case "name":
                sorted.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
                break;
            case "price":
                sorted.sort(Comparator.comparingDouble(Product::getPrice));
                break;
            case "category":
                sorted.sort(Comparator.comparing(p -> p.getCategory().toString()));
                break;
            default:
                // Keep original order
                break;
        }

        return sorted;
    }

    public String generateProductId() {
        List<Product> products = getAllProducts();
        if (products.isEmpty()) {
            return "P001";
        }

        int maxId = products.stream()
                .map(Product::getId)
                .filter(id -> id.matches("P\\d+"))
                .mapToInt(id -> Integer.parseInt(id.substring(1)))
                .max()
                .orElse(0);

        return String.format("P%03d", maxId + 1);
    }
}
