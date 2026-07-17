package com.example.gchc.controller;

import com.example.gchc.dto.ApiResponse;
import com.example.gchc.dto.NewsCategoryDTO;
import com.example.gchc.service.NewsCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news-categories")
public class NewsCategoryController {

    private final NewsCategoryService service;

    public NewsCategoryController(NewsCategoryService service) {
        this.service = service;
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<NewsCategoryDTO>>> getEnabledCategories() {
        return ResponseEntity.ok(ApiResponse.success(service.getEnabledCategories()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NewsCategoryDTO>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsCategoryDTO>> getCategoryById(@PathVariable Long id) {
        return service.getCategoryById(id)
                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NewsCategoryDTO>> createCategory(@RequestBody NewsCategoryDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("创建成功", service.createCategory(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsCategoryDTO>> updateCategory(@PathVariable Long id, @RequestBody NewsCategoryDTO dto) {
        return service.updateCategory(id, dto)
                .map(dto2 -> ResponseEntity.ok(ApiResponse.success("更新成功", dto2)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        if (service.deleteCategory(id)) {
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        }
        return ResponseEntity.notFound().build();
    }
}
