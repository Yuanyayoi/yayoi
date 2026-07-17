
package com.example.gchc.service;

import com.example.gchc.dto.ProductDTO;
import com.example.gchc.entity.Product;
import com.example.gchc.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        List<ProductDTO> products = productRepository.findAllPage(pageable.getOffset(), pageable.getPageSize())
                .stream()
                .map(ProductDTO::fromEntity)
                .toList();
        return new PageImpl<>(products, pageable, productRepository.countAll());
    }

    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        List<ProductDTO> products = productRepository.findByCategoryIdPage(categoryId, pageable.getOffset(), pageable.getPageSize())
                .stream()
                .map(ProductDTO::fromEntity)
                .toList();
        return new PageImpl<>(products, pageable, productRepository.countByCategoryId(categoryId));
    }

    public Page<ProductDTO> getEnabledProducts(Pageable pageable) {
        List<ProductDTO> products = productRepository.findEnabledPage(pageable.getOffset(), pageable.getPageSize())
                .stream()
                .map(ProductDTO::fromEntity)
                .toList();
        return new PageImpl<>(products, pageable, productRepository.countEnabled());
    }

    public List<ProductDTO> getLatestProducts() {
        return productRepository.findTop8ByEnabledTrueOrderByCreatedAtDesc()
                .stream()
                .map(ProductDTO::fromEntity)
                .toList();
    }

    public List<ProductDTO> getHomeProducts() {
        return productRepository.findHomeProducts()
                .stream()
                .map(ProductDTO::fromEntity)
                .toList();
    }

    public List<ProductDTO> getAllEnabledProducts() {
        return productRepository.findAllEnabled()
                .stream()
                .map(ProductDTO::fromEntity)
                .toList();
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id).map(ProductDTO::fromEntity);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setPrice(productDTO.getPrice());
        product.setCategoryId(productDTO.getCategoryId());
        product.setStock(productDTO.getStock() != null ? productDTO.getStock() : 0);
        product.setEnabled(productDTO.getEnabled() != null ? productDTO.getEnabled() : true);
        return ProductDTO.fromEntity(productRepository.save(product));
    }

    public Optional<ProductDTO> updateProduct(Long id, ProductDTO productDTO) {
        return productRepository.findById(id).map(product -> {
            if (productDTO.getName() != null) product.setName(productDTO.getName());
            if (productDTO.getDescription() != null) product.setDescription(productDTO.getDescription());
            if (productDTO.getImage() != null) product.setImage(productDTO.getImage());
            if (productDTO.getPrice() != null) product.setPrice(productDTO.getPrice());
            if (productDTO.getCategoryId() != null) product.setCategoryId(productDTO.getCategoryId());
            if (productDTO.getStock() != null) product.setStock(productDTO.getStock());
            product.setEnabled(productDTO.getEnabled() != null ? productDTO.getEnabled() : product.getEnabled());
            product.setShowOnHome(productDTO.getShowOnHome() != null ? productDTO.getShowOnHome() : product.getShowOnHome());
            return ProductDTO.fromEntity(productRepository.save(product));
        });
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
