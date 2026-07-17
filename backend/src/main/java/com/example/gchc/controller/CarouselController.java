/**
 * 轮播图管理控制器
 * 提供轮播图的增删改查 REST API 接口
 * 所有接口路径前缀: /api/carousels
 */
package com.example.gchc.controller;

import com.example.gchc.entity.Carousel;
import com.example.gchc.repository.CarouselRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @RestController 标识这是一个 REST API 控制器
 * @RequestMapping("/api/carousels") 指定所有接口的基础路径
 * @RequiredArgsConstructor 自动生成构造函数，注入依赖（lombok 注解）
 * @Slf4j 自动生成日志记录器（lombok 注解）
 */
@RestController
@RequestMapping("/api/carousels")
@RequiredArgsConstructor
@Slf4j
public class CarouselController {

    /**
     * 轮播图数据访问层接口
     * 通过 @RequiredArgsConstructor 自动注入
     */
    private final CarouselRepository carouselRepository;

    /**
     * 获取所有轮播图列表
     * GET /api/carousels
     * 
     * @return 包含轮播图列表的响应对象
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCarousels() {
        // 创建响应对象
        Map<String, Object> response = new HashMap<>();
        try {
            // 调用 Repository 方法查询所有轮播图，按创建时间降序排列
            List<Carousel> carousels = carouselRepository.findAllByOrderByCreatedAtDesc();
            response.put("success", true);  // 标记操作成功
            response.put("data", carousels); // 返回轮播图数据
            return ResponseEntity.ok(response); // 返回 HTTP 200 状态码
        } catch (Exception e) {
            // 捕获异常，记录日志
            log.error("获取轮播图列表失败", e);
            response.put("success", false); // 标记操作失败
            response.put("message", "获取轮播图列表失败"); // 返回错误信息
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 根据ID获取单个轮播图
     * GET /api/carousels/{id}
     * 
     * @param id 轮播图ID
     * @return 包含轮播图信息的响应对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCarouselById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 使用 Optional 的 map 方法处理查询结果
            return carouselRepository.findById(id)
                    .map(carousel -> {
                        // 如果找到轮播图
                        response.put("success", true);
                        response.put("data", carousel);
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        // 如果没找到轮播图
                        response.put("success", false);
                        response.put("message", "轮播图不存在");
                        return ResponseEntity.ok(response);
                    });
        } catch (Exception e) {
            log.error("获取轮播图失败", e);
            response.put("success", false);
            response.put("message", "获取轮播图失败");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 创建新的轮播图
     * POST /api/carousels
     * 
     * @param request 包含轮播图信息的请求体
     *                必需字段: title(标题), imageUrl(图片URL)
     *                可选字段: subtitle(副标题), description(描述)
     * @return 包含创建结果的响应对象
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCarousel(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 从请求体中获取参数
            String title = request.get("title");
            String subtitle = request.get("subtitle");
            String description = request.get("description");
            String imageUrl = request.get("imageUrl");

            // 参数校验：标题不能为空
            if (title == null || title.isEmpty()) {
                response.put("success", false);
                response.put("message", "标题不能为空");
                return ResponseEntity.ok(response);
            }
            // 参数校验：图片URL不能为空
            if (imageUrl == null || imageUrl.isEmpty()) {
                response.put("success", false);
                response.put("message", "图片不能为空");
                return ResponseEntity.ok(response);
            }

            // 创建轮播图实体对象
            Carousel carousel = new Carousel();
            carousel.setTitle(title);
            carousel.setSubtitle(subtitle);
            carousel.setDescription(description);
            carousel.setImageUrl(imageUrl);

            // 保存到数据库
            Carousel savedCarousel = carouselRepository.save(carousel);
            log.info("轮播图保存成功: {}", savedCarousel.getId());

            // 构建成功响应
            response.put("success", true);
            response.put("data", savedCarousel);
            response.put("message", "保存成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("保存轮播图失败", e);
            response.put("success", false);
            response.put("message", "保存失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 更新轮播图信息
     * PUT /api/carousels/{id}
     * 
     * @param id 要更新的轮播图ID
     * @param request 包含更新信息的请求体
     * @return 包含更新结果的响应对象
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCarousel(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            return carouselRepository.findById(id)
                    .map(existingCarousel -> {
                        // 获取更新参数
                        String title = request.get("title");
                        String subtitle = request.get("subtitle");
                        String description = request.get("description");
                        String imageUrl = request.get("imageUrl");

                        // 更新非空字段（如果请求中提供了该字段）
                        if (title != null) existingCarousel.setTitle(title);
                        if (subtitle != null) existingCarousel.setSubtitle(subtitle);
                        if (description != null) existingCarousel.setDescription(description);
                        if (imageUrl != null) existingCarousel.setImageUrl(imageUrl);

                        // 保存更新后的实体
                        Carousel updatedCarousel = carouselRepository.save(existingCarousel);
                        log.info("轮播图更新成功: {}", id);

                        response.put("success", true);
                        response.put("data", updatedCarousel);
                        response.put("message", "更新成功");
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        // 如果轮播图不存在
                        response.put("success", false);
                        response.put("message", "轮播图不存在");
                        return ResponseEntity.ok(response);
                    });
        } catch (Exception e) {
            log.error("更新轮播图失败", e);
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 删除轮播图
     * DELETE /api/carousels/{id}
     * 
     * @param id 要删除的轮播图ID
     * @return 包含删除结果的响应对象
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCarousel(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查轮播图是否存在
            if (carouselRepository.existsById(id)) {
                // 删除轮播图
                carouselRepository.deleteById(id);
                log.info("轮播图删除成功: {}", id);
                response.put("success", true);
                response.put("message", "删除成功");
            } else {
                // 如果轮播图不存在
                response.put("success", false);
                response.put("message", "轮播图不存在");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除轮播图失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}