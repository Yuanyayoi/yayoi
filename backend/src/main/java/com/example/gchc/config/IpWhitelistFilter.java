package com.example.gchc.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IpWhitelistFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(IpWhitelistFilter.class);

    private final boolean enabled;
    private final List<String> allowedIpsConfig;
    private final List<byte[][]> allowedRanges = new ArrayList<>();

    private static final String[] PROTECTED_PREFIXES = {
        "/admin",
        "/auth",
        "/api/upload",
        "/api/admin",
        "/api/site",
        "/api/news-categories",
        "/api/products",
        "/api/core-values",
        "/api/milestones",
        "/api/awards",
        "/api/messages",
        "/articles",
        "/categories"
    };

    private static final String[] PUBLIC_PREFIXES = {
        "/api/carousels",
        "/api/features",
        "/api/icons/public",
        "/api/products/public",
        "/api/news-categories/public",
        "/api/core-values/public",
        "/api/milestones/public",
        "/api/awards/public",
        "/api/site/hero",
        "/api/site/about",
        "/api/site/contact",
        "/api/site/footer",
        "/api/site/global",
        "/api/site/values",
        "/api/site/milestones",
        "/api/site/team",
        "/api/site/product-hero",
        "/api/site/news-hero",
        "/api/site/contact-hero",
        "/api/site/modules",
        "/api/site/home-products",
        "/articles/public",
        "/categories/public",
        "/messages/public",
        "/products/public",
        "/site/"
    };

    public IpWhitelistFilter(boolean enabled, List<String> allowedIpsConfig) {
        this.enabled = enabled;
        this.allowedIpsConfig = allowedIpsConfig != null ? allowedIpsConfig : new ArrayList<>();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (!enabled) {
            logger.info("========== IP白名单过滤器未启用 ==========");
            return;
        }
        parseAllowedIps();
        logger.info("========== IP白名单过滤器已启用 ==========");
        logger.info("允许的IP/CIDR: {}", allowedIpsConfig);
        for (byte[][] range : allowedRanges) {
            logger.info("  解析网段: network={}, mask={}", bytesToIp(range[0]), bytesToIp(range[1]));
        }
    }

    private String bytesToIp(byte[] bytes) {
        if (bytes.length == 4) {
            return (bytes[0] & 0xFF) + "." + (bytes[1] & 0xFF) + "." + (bytes[2] & 0xFF) + "." + (bytes[3] & 0xFF);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i += 2) {
            if (i > 0) sb.append(":");
            sb.append(String.format("%02x%02x", bytes[i] & 0xFF, bytes[i + 1] & 0xFF));
        }
        return sb.toString();
    }

    private void parseAllowedIps() {
        allowedRanges.clear();
        List<String> cidrs = new ArrayList<>();

        cidrs.add("127.0.0.1/32");
        cidrs.add("::1/128");

        for (String part : allowedIpsConfig) {
            if (part == null) continue;
            String cidr = part.trim();
            int hashIdx = cidr.indexOf('#');
            if (hashIdx >= 0) {
                cidr = cidr.substring(0, hashIdx).trim();
            }
            if (cidr.isEmpty()) continue;
            if (!cidr.contains("/")) {
                InetAddress addr;
                try {
                    addr = InetAddress.getByName(cidr);
                    cidr = cidr + "/" + (addr.getAddress().length * 8);
                } catch (UnknownHostException e) {
                    logger.warn("无法解析IP: {}", cidr);
                    continue;
                }
            }
            if (!cidrs.contains(cidr)) {
                cidrs.add(cidr);
            }
        }

        for (String cidr : cidrs) {
            try {
                String[] parts = cidr.split("/");
                InetAddress addr = InetAddress.getByName(parts[0]);
                int prefixLen = Integer.parseInt(parts[1]);
                byte[] addrBytes = addr.getAddress();
                byte[] mask = prefixToMask(prefixLen, addrBytes.length);
                byte[] network = applyMask(addrBytes, mask);
                allowedRanges.add(new byte[][]{network, mask});
                logger.info("  已添加白名单: {} (network={}, mask={})", cidr, bytesToIp(network), bytesToIp(mask));
            } catch (UnknownHostException e) {
                logger.warn("无法解析白名单CIDR: {}", cidr);
            }
        }
    }

    private byte[] prefixToMask(int prefixLen, int byteLen) {
        byte[] mask = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            int bits = Math.min(8, Math.max(0, prefixLen - i * 8));
            mask[i] = (byte) (bits == 0 ? 0 : (0xFF << (8 - bits)) & 0xFF);
        }
        return mask;
    }

    private byte[] applyMask(byte[] addr, byte[] mask) {
        byte[] network = new byte[addr.length];
        for (int i = 0; i < addr.length; i++) {
            network[i] = (byte) (addr[i] & mask[i]);
        }
        return network;
    }

    private boolean isAllowed(String clientIp) {
        try {
            InetAddress addr = InetAddress.getByName(clientIp);
            if (addr.isLoopbackAddress() || addr.isAnyLocalAddress()) {
                return true;
            }
            byte[] clientBytes = addr.getAddress();
            for (byte[][] range : allowedRanges) {
                byte[] network = range[0];
                byte[] mask = range[1];
                if (network.length != clientBytes.length) continue;
                boolean match = true;
                for (int i = 0; i < clientBytes.length; i++) {
                    if ((clientBytes[i] & mask[i]) != network[i]) {
                        match = false;
                        break;
                    }
                }
                if (match) return true;
            }
        } catch (UnknownHostException e) {
            logger.warn("无法解析客户端IP: {}", clientIp);
        }
        return false;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            int comma = ip.indexOf(',');
            return comma > 0 ? ip.substring(0, comma).trim() : ip.trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isBlank() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        return request.getRemoteAddr();
    }

    private boolean isPublicPath(String uri, String method) {
        if (uri.equals("/") || uri.equals("/index.html") || uri.equals("/error")) return true;
        if (uri.startsWith("/assets/") || uri.startsWith("/uploads/")) return true;
        if (uri.startsWith("/favicon")) return true;

        for (String pub : PUBLIC_PREFIXES) {
            if (uri.startsWith(pub)) return true;
        }

        if (uri.equals("/about") || uri.equals("/contact")) return true;
        if (uri.matches("^/products(/[0-9]+)?$")) return true;
        if (uri.matches("^/news(/[0-9]+)?$")) return true;

        for (String prot : PROTECTED_PREFIXES) {
            if (uri.startsWith(prot)) {
                for (String pub : PUBLIC_PREFIXES) {
                    if (uri.startsWith(pub)) return true;
                }
                return false;
            }
        }

        return true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String uri = httpReq.getRequestURI();
        String method = httpReq.getMethod();
        String clientIp = getClientIp(httpReq);

        if ("OPTIONS".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        boolean isPublic = isPublicPath(uri, method);

        if (!isPublic) {
            logger.info("IP白名单检查: clientIp=[{}], method={}, uri=[{}]", clientIp, method, uri);
        }

        if (enabled && !isPublic) {
            if (!isAllowed(clientIp)) {
                logger.warn("IP白名单拦截: 拒绝IP [{}] 访问 [{} {}]", clientIp, method, uri);
                httpRes.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpRes.setContentType("application/json;charset=UTF-8");
                httpRes.getWriter().write("{\"code\":403,\"message\":\"Access denied: IP not in whitelist (" + clientIp + ")\",\"data\":null}");
                httpRes.getWriter().flush();
                return;
            } else {
                logger.info("IP白名单放行: IP [{}] 允许访问 [{} {}]", clientIp, method, uri);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
