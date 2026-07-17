package com.example.gchc.controller;

import com.example.gchc.entity.Message;
import com.example.gchc.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/messages", "/messages"})
public class MessageController {

    private final MessageRepository messageRepository;

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[\\d\\s\\-.()]{4,25}(?:\\s*(?:ext\\.?|x|分机|转)\\s*\\d{1,6})?$"
    );

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    );

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        String cleaned = phone.trim();
        long digitCount = cleaned.chars().filter(Character::isDigit).count();
        if (digitCount < 4 || digitCount > 15) return false;
        return PHONE_PATTERN.matcher(cleaned).matches();
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return true;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    @PostMapping("/public")
    public ResponseEntity<Map<String, Object>> createMessage(@RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        try {
            String name = body.get("name") != null ? ((String) body.get("name")).trim() : "";
            String email = body.get("email") != null ? ((String) body.get("email")).trim() : "";
            String phone = body.get("phone") != null ? ((String) body.get("phone")).trim() : "";
            String messageText = body.get("message") != null ? ((String) body.get("message")).trim() : "";
            String company = body.get("company") != null ? ((String) body.get("company")).trim() : "";

            if (name.isEmpty()) {
                response.put("code", 400);
                response.put("message", "请填写姓名");
                return ResponseEntity.ok(response);
            }
            if (phone.isEmpty()) {
                response.put("code", 400);
                response.put("message", "请填写联系电话");
                return ResponseEntity.ok(response);
            }
            if (!isValidPhone(phone)) {
                response.put("code", 400);
                response.put("message", "请输入有效的电话号码（支持手机、座机、国际号码）");
                return ResponseEntity.ok(response);
            }
            if (!email.isEmpty() && !isValidEmail(email)) {
                response.put("code", 400);
                response.put("message", "请输入有效的邮箱地址");
                return ResponseEntity.ok(response);
            }
            if (messageText.isEmpty()) {
                response.put("code", 400);
                response.put("message", "请填写留言内容");
                return ResponseEntity.ok(response);
            }

            Message msg = new Message();
            msg.setName(name);
            msg.setEmail(email);
            msg.setPhone(phone);
            msg.setMessage(messageText);
            messageRepository.insert(msg);
            response.put("code", 200);
            response.put("message", "留言提交成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("留言提交失败", e);
            response.put("code", 500);
            response.put("message", "留言提交失败，请稍后重试");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMessages(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Message> messages = messageRepository.findByCondition(status, keyword);
            response.put("code", 200);
            response.put("data", messages);
            response.put("total", messageRepository.countByCondition(status, keyword));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取留言列表失败", e);
            response.put("code", 500);
            response.put("message", "获取留言列表失败");
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMessage(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("尝试删除留言, id={}", id);
            int affected = messageRepository.deleteById(id);
            log.info("删除留言结果, id={}, affected={}", id, affected);
            response.put("code", 200);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除留言失败, id={}, error={}", id, e.getMessage(), e);
            response.put("code", 500);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/{id}/reply")
    public ResponseEntity<Map<String, Object>> markReplied(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            messageRepository.markAsReplied(id);
            response.put("code", 200);
            response.put("message", "已标记为已回复");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("标记已回复失败", e);
            response.put("code", 500);
            response.put("message", "操作失败");
            return ResponseEntity.status(500).body(response);
        }
    }
}
