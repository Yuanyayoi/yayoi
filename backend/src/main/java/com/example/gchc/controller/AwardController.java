package com.example.gchc.controller;

import com.example.gchc.dto.ApiResponse;
import com.example.gchc.dto.AwardDTO;
import com.example.gchc.service.AwardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/awards")
public class AwardController {

    private final AwardService awardService;

    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<AwardDTO>>> getPublicAwards() {
        List<AwardDTO> awards = awardService.getAllAwards();
        return ResponseEntity.ok(ApiResponse.success(awards));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AwardDTO>>> getAllAwards() {
        List<AwardDTO> awards = awardService.getAllAwards();
        return ResponseEntity.ok(ApiResponse.success(awards));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AwardDTO>> getAwardById(@PathVariable Long id) {
        AwardDTO award = awardService.getAwardById(id);
        if (award == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(award));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AwardDTO>> createAward(@RequestBody AwardDTO dto) {
        AwardDTO created = awardService.createAward(dto);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AwardDTO>> updateAward(@PathVariable Long id, @RequestBody AwardDTO dto) {
        AwardDTO updated = awardService.updateAward(id, dto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAward(@PathVariable Long id) {
        boolean deleted = awardService.deleteAward(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
