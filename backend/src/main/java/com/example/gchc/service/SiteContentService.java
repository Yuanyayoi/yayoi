package com.example.gchc.service;

import com.example.gchc.entity.SiteSetting;
import com.example.gchc.entity.TeamMember;
import com.example.gchc.repository.SiteSettingRepository;
import com.example.gchc.repository.TeamMemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SiteContentService {

    private static final Map<String, String> ABOUT_DEFAULTS = new LinkedHashMap<>();
    static {
        ABOUT_DEFAULTS.put("companyName", "");
        ABOUT_DEFAULTS.put("companyDesc", "");
        ABOUT_DEFAULTS.put("mission", "");
        ABOUT_DEFAULTS.put("vision", "");
        ABOUT_DEFAULTS.put("values", "");
        ABOUT_DEFAULTS.put("image", "");
    }

    private static final Map<String, String> CONTACT_DEFAULTS = new LinkedHashMap<>();
    static {
        CONTACT_DEFAULTS.put("address", "");
        CONTACT_DEFAULTS.put("phone", "");
        CONTACT_DEFAULTS.put("email", "");
        CONTACT_DEFAULTS.put("businessHours", "");
        CONTACT_DEFAULTS.put("description", "");
    }

    private static final Map<String, String> FOOTER_DEFAULTS = new LinkedHashMap<>();
    static {
        FOOTER_DEFAULTS.put("brandDescription", "四川国驰恒创科技有限公司专注新能源电池快充技术解决方案，为动力电池、储能电池及多场景移动电力提供从研发验证到产业化导入的一体化能力。");
        FOOTER_DEFAULTS.put("copyright", "© 2024 四川国驰恒创科技有限公司. 保留所有权利.");
        FOOTER_DEFAULTS.put("privacyText", "隐私政策");
        FOOTER_DEFAULTS.put("termsText", "使用条款");
        FOOTER_DEFAULTS.put("sitemapText", "网站地图");
        FOOTER_DEFAULTS.put("service1", "关于我们");
        FOOTER_DEFAULTS.put("service1Url", "/about");
        FOOTER_DEFAULTS.put("service2", "产品服务");
        FOOTER_DEFAULTS.put("service2Url", "/products");
        FOOTER_DEFAULTS.put("service3", "");
        FOOTER_DEFAULTS.put("service3Url", "");
        FOOTER_DEFAULTS.put("service4", "");
        FOOTER_DEFAULTS.put("service4Url", "");
        FOOTER_DEFAULTS.put("service5", "");
        FOOTER_DEFAULTS.put("service5Url", "");
    }

    private static final Map<String, String> GLOBAL_DEFAULTS = new LinkedHashMap<>();
    static {
        GLOBAL_DEFAULTS.put("siteName", "四川国驰恒创科技有限公司");
        GLOBAL_DEFAULTS.put("siteDescription", "专注新能源电池快充技术解决方案");
        GLOBAL_DEFAULTS.put("logoUrl", "/favicon-color.png");
        GLOBAL_DEFAULTS.put("logoUrlDark", "/favicon.png");
    }

    private static final Map<String, String> VALUES_DEFAULTS = new LinkedHashMap<>();
    static {
        VALUES_DEFAULTS.put("value1Title", "");
        VALUES_DEFAULTS.put("value1Icon", "");
        VALUES_DEFAULTS.put("value1Desc", "");
        VALUES_DEFAULTS.put("value2Title", "");
        VALUES_DEFAULTS.put("value2Icon", "");
        VALUES_DEFAULTS.put("value2Desc", "");
        VALUES_DEFAULTS.put("value3Title", "");
        VALUES_DEFAULTS.put("value3Icon", "");
        VALUES_DEFAULTS.put("value3Desc", "");
        VALUES_DEFAULTS.put("value4Title", "");
        VALUES_DEFAULTS.put("value4Icon", "");
        VALUES_DEFAULTS.put("value4Desc", "");
    }

    private static final Map<String, String> PRODUCT_HERO_DEFAULTS = new LinkedHashMap<>();
    static {
        PRODUCT_HERO_DEFAULTS.put("heroTitle", "");
        PRODUCT_HERO_DEFAULTS.put("heroSubtitle", "");
        PRODUCT_HERO_DEFAULTS.put("heroBgImage", "");
    }

    private static final Map<String, String> NEWS_HERO_DEFAULTS = new LinkedHashMap<>();
    static {
        NEWS_HERO_DEFAULTS.put("heroTitle", "新闻动态");
        NEWS_HERO_DEFAULTS.put("heroSubtitle", "了解公司最新资讯与行业动态");
        NEWS_HERO_DEFAULTS.put("heroBgImage", "");
    }

    private static final Map<String, String> CONTACT_HERO_DEFAULTS = new LinkedHashMap<>();
    static {
        CONTACT_HERO_DEFAULTS.put("heroTitle", "联系我们");
        CONTACT_HERO_DEFAULTS.put("heroSubtitle", "有任何问题，欢迎随时与我们联系。");
        CONTACT_HERO_DEFAULTS.put("heroBgImage", "");
    }

    private static final Map<String, String> CONTACT_ONLINE_DEFAULTS = new LinkedHashMap<>();
    static {
        CONTACT_ONLINE_DEFAULTS.put("title", "在线咨询");
        CONTACT_ONLINE_DEFAULTS.put("subtitle", "发送消息给我们");
        CONTACT_ONLINE_DEFAULTS.put("description", "如有任何问题或合作意向，请填写以下表单，我们的专业团队将在24小时内与您联系。");
        CONTACT_ONLINE_DEFAULTS.put("feature1Title", "专业团队一对一服务");
        CONTACT_ONLINE_DEFAULTS.put("feature2Title", "快速响应您的需求");
        CONTACT_ONLINE_DEFAULTS.put("feature3Title", "免费技术方案咨询");
    }

    private static final Map<String, String> CONTACT_FORM_DEFAULTS = new LinkedHashMap<>();
    static {
        CONTACT_FORM_DEFAULTS.put("nameLabel", "姓名");
        CONTACT_FORM_DEFAULTS.put("namePlaceholder", "请输入您的姓名");
        CONTACT_FORM_DEFAULTS.put("phoneLabel", "电话");
        CONTACT_FORM_DEFAULTS.put("phonePlaceholder", "请输入您的联系电话");
        CONTACT_FORM_DEFAULTS.put("emailLabel", "邮箱");
        CONTACT_FORM_DEFAULTS.put("emailPlaceholder", "请输入您的邮箱地址");
        CONTACT_FORM_DEFAULTS.put("companyLabel", "公司");
        CONTACT_FORM_DEFAULTS.put("companyPlaceholder", "请输入您的公司名称");
        CONTACT_FORM_DEFAULTS.put("messageLabel", "留言内容");
        CONTACT_FORM_DEFAULTS.put("messagePlaceholder", "请详细描述您的需求或问题，以便我们为您提供更精准的服务...");
        CONTACT_FORM_DEFAULTS.put("submitButtonText", "提交留言");
    }

    private static final Map<String, String> FEATURES_DEFAULTS = new LinkedHashMap<>();
    static {
        FEATURES_DEFAULTS.put("sectionSubtitle", "为什么选择我们");
        FEATURES_DEFAULTS.put("sectionTitle", "核心优势");
    }

    private static final Map<String, String> HOME_HERO_DEFAULTS = new LinkedHashMap<>();
    static {
        HOME_HERO_DEFAULTS.put("companyName", "四川国驰恒创科技有限公司");
        HOME_HERO_DEFAULTS.put("companyDesc", "核心技术源自美国辛辛那提大学博士团队，专注于提供新能源电池快充技术解决方案。");
    }

    private static final Map<String, String> ABOUT_HERO_DEFAULTS = new LinkedHashMap<>();
    static {
        ABOUT_HERO_DEFAULTS.put("companyName", "四川国驰恒创科技有限公司");
        ABOUT_HERO_DEFAULTS.put("companyDesc", "核心技术源自美国辛辛那提大学博士团队，专注于提供新能源电池快充技术解决方案。");
        ABOUT_HERO_DEFAULTS.put("backgroundImage", "");
    }

    private static final Map<String, String> HOME_ABOUT_INTRO_DEFAULTS = new LinkedHashMap<>();
    static {
        HOME_ABOUT_INTRO_DEFAULTS.put("subtitle", "关于国驰恒创");
        HOME_ABOUT_INTRO_DEFAULTS.put("buttonText", "了解更多");
        HOME_ABOUT_INTRO_DEFAULTS.put("buttonLink", "/about");
        HOME_ABOUT_INTRO_DEFAULTS.put("imageUrl", "");
    }

    private static final Map<String, String> HOME_NEWS_DEFAULTS = new LinkedHashMap<>();
    static {
        HOME_NEWS_DEFAULTS.put("sectionSubtitle", "最新动态");
        HOME_NEWS_DEFAULTS.put("sectionTitle", "公司新闻");
        HOME_NEWS_DEFAULTS.put("buttonText", "查看全部文章");
        HOME_NEWS_DEFAULTS.put("buttonLink", "/news");
    }

    private static final Map<String, String> HOME_PRODUCTS_DEFAULTS = new LinkedHashMap<>();
    static {
        HOME_PRODUCTS_DEFAULTS.put("sectionSubtitle", "热门产品");
        HOME_PRODUCTS_DEFAULTS.put("sectionTitle", "我们的产品");
        HOME_PRODUCTS_DEFAULTS.put("buttonText", "查看全部产品");
        HOME_PRODUCTS_DEFAULTS.put("buttonLink", "/products");
    }

    private static final Map<String, String> HOME_CTA_DEFAULTS = new LinkedHashMap<>();
    static {
        HOME_CTA_DEFAULTS.put("eyebrow", "立即行动");
        HOME_CTA_DEFAULTS.put("title", "准备好开启新能源技术合作了吗？");
        HOME_CTA_DEFAULTS.put("description", "无论是电池快充技术、储能系统还是智能装备，我们的专家团队为您提供从研发到量产的一站式解决方案");
        HOME_CTA_DEFAULTS.put("button1Text", "立即咨询");
        HOME_CTA_DEFAULTS.put("button1Link", "/contact");
        HOME_CTA_DEFAULTS.put("button2Text", "了解产品");
        HOME_CTA_DEFAULTS.put("button2Link", "/products");
    }

    private static final Map<String, String> MILESTONES_DEFAULTS = new LinkedHashMap<>();
    static {
        MILESTONES_DEFAULTS.put("milestone1Year", "");
        MILESTONES_DEFAULTS.put("milestone1Desc", "");
        MILESTONES_DEFAULTS.put("milestone2Year", "");
        MILESTONES_DEFAULTS.put("milestone2Desc", "");
        MILESTONES_DEFAULTS.put("milestone3Year", "");
        MILESTONES_DEFAULTS.put("milestone3Desc", "");
        MILESTONES_DEFAULTS.put("milestone4Year", "");
        MILESTONES_DEFAULTS.put("milestone4Desc", "");
    }

    private final SiteSettingRepository siteSettingRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ObjectMapper objectMapper;

    public SiteContentService(SiteSettingRepository siteSettingRepository,
                              TeamMemberRepository teamMemberRepository) {
        this.siteSettingRepository = siteSettingRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, String> getSettings(String groupName) {
        Map<String, String> values = new LinkedHashMap<>(defaultsFor(groupName));
        siteSettingRepository.findByGroupName(groupName)
                .forEach(setting -> values.put(setting.getSettingKey(), setting.getSettingValue()));
        return values;
    }

    public Map<String, String> saveSettings(String groupName, Map<String, String> values) {
        values.forEach((key, value) -> {
            SiteSetting setting = siteSettingRepository
                    .findByGroupNameAndSettingKey(groupName, key)
                    .orElseGet(() -> {
                        SiteSetting created = new SiteSetting();
                        created.setGroupName(groupName);
                        created.setSettingKey(key);
                        return created;
                    });
            setting.setSettingValue(value);
            siteSettingRepository.save(setting);
        });
        return getSettings(groupName);
    }

    public Map<String, Object> getContentModules() {
        Map<String, Object> modules = new LinkedHashMap<>();
        siteSettingRepository.findByGroupName("contentModules").forEach(setting -> {
            String value = setting.getSettingValue();
            if (value == null || value.isBlank()) {
                modules.put(setting.getSettingKey(), Map.of());
                return;
            }
            try {
                modules.put(setting.getSettingKey(),
                        objectMapper.readValue(value, new TypeReference<Map<String, Object>>() {}));
            } catch (Exception ignored) {
                modules.put(setting.getSettingKey(), value);
            }
        });
        return modules;
    }

    public Map<String, Object> saveContentModules(Map<String, Object> modules) {
        modules.forEach((key, value) -> {
            SiteSetting setting = siteSettingRepository
                    .findByGroupNameAndSettingKey("contentModules", key)
                    .orElseGet(() -> {
                        SiteSetting created = new SiteSetting();
                        created.setGroupName("contentModules");
                        created.setSettingKey(key);
                        return created;
                    });
            try {
                setting.setSettingValue(objectMapper.writeValueAsString(value));
            } catch (Exception e) {
                setting.setSettingValue(String.valueOf(value));
            }
            siteSettingRepository.save(setting);
        });
        return getContentModules();
    }

    public List<TeamMember> getTeamMembers(boolean publicOnly) {
        if (publicOnly) {
            return teamMemberRepository.findByEnabledTrueOrderBySortOrderAscCreatedAtDesc();
        }
        return teamMemberRepository.findAllByOrderBySortOrderAscCreatedAtDesc();
    }

    public TeamMember createTeamMember(TeamMember request) {
        TeamMember member = new TeamMember();
        copyTeamMember(request, member);
        return teamMemberRepository.save(member);
    }

    public TeamMember updateTeamMember(Long id, TeamMember request) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team member not found"));
        copyTeamMember(request, member);
        return teamMemberRepository.save(member);
    }

    public boolean deleteTeamMember(Long id) {
        if (!teamMemberRepository.existsById(id)) {
            return false;
        }
        teamMemberRepository.deleteById(id);
        return true;
    }

    private Map<String, String> defaultsFor(String groupName) {
        switch (groupName) {
            case "about": return ABOUT_DEFAULTS;
            case "contact": return CONTACT_DEFAULTS;
            case "footer": return FOOTER_DEFAULTS;
            case "global": return GLOBAL_DEFAULTS;
            case "values": return VALUES_DEFAULTS;
            case "milestones": return MILESTONES_DEFAULTS;
            case "productHero": return PRODUCT_HERO_DEFAULTS;
            case "newsHero": return NEWS_HERO_DEFAULTS;
            case "contactHero": return CONTACT_HERO_DEFAULTS;
            case "contactOnline": return CONTACT_ONLINE_DEFAULTS;
            case "contactForm": return CONTACT_FORM_DEFAULTS;
            case "features": return FEATURES_DEFAULTS;
            case "homeHero": return HOME_HERO_DEFAULTS;
            case "aboutHero": return ABOUT_HERO_DEFAULTS;
            case "homeAboutIntro": return HOME_ABOUT_INTRO_DEFAULTS;
            case "homeNews": return HOME_NEWS_DEFAULTS;
            case "homeProducts": return HOME_PRODUCTS_DEFAULTS;
            case "homeCta": return HOME_CTA_DEFAULTS;
            default: return Map.of();
        }
    }

    private void copyTeamMember(TeamMember source, TeamMember target) {
        target.setName(source.getName());
        target.setPosition(source.getPosition());
        target.setBio(source.getBio());
        target.setAvatar(source.getAvatar());
        target.setSortOrder(source.getSortOrder() == null ? 0 : source.getSortOrder());
        target.setEnabled(source.getEnabled() == null || source.getEnabled());
    }
}
