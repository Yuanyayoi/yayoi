
package com.example.gchc.controller;

import com.example.gchc.dto.ApiResponse;
import com.example.gchc.dto.ProductDTO;
import com.example.gchc.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getEnabledProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProductDTO> products = productService.getEnabledProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/public/latest")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getLatestProducts() {
        List<ProductDTO> products = productService.getLatestProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/public/home")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getHomeProducts() {
        List<ProductDTO> products = productService.getHomeProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/public/all")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllEnabledProducts() {
        List<ProductDTO> products = productService.getAllEnabledProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(ApiResponse.success(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProductDTO> products = categoryId != null
                ? productService.getProductsByCategory(categoryId, pageable)
                : productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(ApiResponse.success("创建成功", createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO)
                .map(product -> ResponseEntity.ok(ApiResponse.success("更新成功", product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        }
        return ResponseEntity.notFound().build();
    }
}
