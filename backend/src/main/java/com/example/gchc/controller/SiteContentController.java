package com.example.gchc.controller;

import com.example.gchc.dto.ApiResponse;
import com.example.gchc.entity.TeamMember;
import com.example.gchc.service.SiteContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SiteContentController {

    private final SiteContentService siteContentService;

    public SiteContentController(SiteContentService siteContentService) {
        this.siteContentService = siteContentService;
    }

    @GetMapping("/site/about")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPublicAbout() {
        return ResponseEntity.ok(ApiResponse.success(aboutPayload(true)));
    }

    @GetMapping("/site/contact")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicContact() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("contact")));
    }

    @GetMapping("/site/modules")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPublicModules() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getContentModules()));
    }

    @GetMapping("/site/footer")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicFooter() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("footer")));
    }

    @GetMapping("/site/global")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicGlobal() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("global")));
    }

    @GetMapping("/site/values")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicValues() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("values")));
    }

    @GetMapping("/site/milestones")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicMilestones() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("milestones")));
    }

    @GetMapping("/site/features")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicFeatures() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("features")));
    }

    @GetMapping("/api/site/about")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminAbout() {
        return ResponseEntity.ok(ApiResponse.success(aboutPayload(false)));
    }

    @PutMapping("/api/site/about")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveAbout(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("about", request)));
    }

    @GetMapping("/api/site/contact")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminContact() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("contact")));
    }

    @PutMapping("/api/site/contact")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveContact(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("contact", request)));
    }

    @GetMapping("/api/site/modules")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminModules() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getContentModules()));
    }

    @PutMapping("/api/site/modules")
    public ResponseEntity<ApiResponse<Map<String, Object>>> saveAdminModules(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveContentModules(request)));
    }

    @GetMapping("/api/site/team")
    public ResponseEntity<ApiResponse<List<TeamMember>>> getTeamMembers() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getTeamMembers(false)));
    }

    @PostMapping("/api/site/team")
    public ResponseEntity<ApiResponse<TeamMember>> createTeamMember(@RequestBody TeamMember request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.createTeamMember(request)));
    }

    @PutMapping("/api/site/team/{id}")
    public ResponseEntity<ApiResponse<TeamMember>> updateTeamMember(@PathVariable Long id,
                                                                    @RequestBody TeamMember request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.updateTeamMember(id, request)));
    }

    @DeleteMapping("/api/site/team/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeamMember(@PathVariable Long id) {
        if (siteContentService.deleteTeamMember(id)) {
            return ResponseEntity.ok(ApiResponse.success("Deleted", null));
        }
        return ResponseEntity.notFound().build();
    }

    // ===== 页脚设置 =====
    @GetMapping("/api/site/footer")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminFooter() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("footer")));
    }

    @PutMapping("/api/site/footer")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveFooter(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("footer", request)));
    }

    // ===== 全局设置 =====
    @GetMapping("/api/site/global")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminGlobal() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("global")));
    }

    @PutMapping("/api/site/global")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveGlobal(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("global", request)));
    }

    // ===== 核心价值观 =====
    @GetMapping("/api/site/values")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminValues() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("values")));
    }

    @PutMapping("/api/site/values")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveValues(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("values", request)));
    }

    // ===== 发展历程 =====
    @GetMapping("/api/site/milestones")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminMilestones() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("milestones")));
    }

    @PutMapping("/api/site/milestones")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveMilestones(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("milestones", request)));
    }

    // ===== 核心优势页面文本 =====
    @GetMapping("/api/site/features")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminFeatures() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("features")));
    }

    @PutMapping("/api/site/features")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveFeatures(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("features", request)));
    }

    // ===== 首页 Hero 横幅模块 =====
    @GetMapping("/site/home-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicHomeHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeHero")));
    }

    @GetMapping("/api/site/home-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminHomeHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeHero")));
    }

    @PutMapping("/api/site/home-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveHomeHero(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("homeHero", request)));
    }

    // ===== 关于我们 Hero 横幅模块 =====
    @GetMapping("/api/site/about-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminAboutHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("aboutHero")));
    }

    @PutMapping("/api/site/about-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveAboutHero(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("aboutHero", request)));
    }

    // ===== 首页公司简介模块 =====
    @GetMapping("/site/home-about-intro")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicHomeAboutIntro() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeAboutIntro")));
    }

    @GetMapping("/api/site/home-about-intro")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminHomeAboutIntro() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeAboutIntro")));
    }

    @PutMapping("/api/site/home-about-intro")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveHomeAboutIntro(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("homeAboutIntro", request)));
    }

    // ===== 首页最新动态模块 =====
    @GetMapping("/site/home-news")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicHomeNews() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeNews")));
    }

    @GetMapping("/api/site/home-news")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminHomeNews() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeNews")));
    }

    @PutMapping("/api/site/home-news")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveHomeNews(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("homeNews", request)));
    }

    // ===== 首页热门产品模块 =====
    @GetMapping("/site/home-products")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicHomeProducts() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeProducts")));
    }

    @GetMapping("/api/site/home-products")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminHomeProducts() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeProducts")));
    }

    @PutMapping("/api/site/home-products")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveHomeProducts(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("homeProducts", request)));
    }

    // ===== 首页立即行动模块 =====
    @GetMapping("/site/home-cta")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicHomeCta() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeCta")));
    }

    @GetMapping("/api/site/home-cta")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminHomeCta() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("homeCta")));
    }

    @PutMapping("/api/site/home-cta")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveHomeCta(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("homeCta", request)));
    }

    // ===== 产品页面 Hero 文本 =====
    @GetMapping("/site/product-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicProductHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("productHero")));
    }

    @GetMapping("/api/site/product-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminProductHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("productHero")));
    }

    @PutMapping("/api/site/product-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveProductHero(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("productHero", request)));
    }

    // ===== 新闻页面 Hero 文本 =====
    @GetMapping("/site/news-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicNewsHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("newsHero")));
    }

    @GetMapping("/api/site/news-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminNewsHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("newsHero")));
    }

    @PutMapping("/api/site/news-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveNewsHero(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("newsHero", request)));
    }

    // ===== 联系我们页面 Hero 文案 =====
    @GetMapping("/site/contact-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicContactHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("contactHero")));
    }

    @GetMapping("/api/site/contact-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminContactHero() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("contactHero")));
    }

    @PutMapping("/api/site/contact-hero")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveContactHero(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("contactHero", request)));
    }

    // ===== 联系我们 - 在线咨询文案 =====
    @GetMapping("/site/contact-online")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicContactOnline() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("contactOnline")));
    }

    @GetMapping("/api/site/contact-online")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminContactOnline() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("contactOnline")));
    }

    @PutMapping("/api/site/contact-online")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveContactOnline(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("contactOnline", request)));
    }

    // ===== 联系我们 - 提交留言文案 =====
    @GetMapping("/site/contact-form")
    public ResponseEntity<ApiResponse<Map<String, String>>> getPublicContactForm() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("contactForm")));
    }

    @GetMapping("/api/site/contact-form")
    public ResponseEntity<ApiResponse<Map<String, String>>> getAdminContactForm() {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.getSettings("contactForm")));
    }

    @PutMapping("/api/site/contact-form")
    public ResponseEntity<ApiResponse<Map<String, String>>> saveContactForm(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success(siteContentService.saveSettings("contactForm", request)));
    }

    private Map<String, Object> aboutPayload(boolean publicOnly) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.putAll(siteContentService.getSettings("about"));
        payload.put("teamMembers", siteContentService.getTeamMembers(publicOnly));
        return payload;
    }
}
