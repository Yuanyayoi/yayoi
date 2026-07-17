/**
 * 统一 API 响应类
 * 用于封装所有 API 的响应数据，使前后端交互格式统一
 * 
 * @param <T> 泛型类型，表示响应数据的类型
 */
package com.example.gchc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @Data 自动生成 getter、setter、toString、equals、hashCode 方法
 * @NoArgsConstructor 自动生成无参构造函数
 * @AllArgsConstructor 自动生成全参构造函数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * 响应状态码
     * 200: 成功
     * 500: 服务器内部错误
     * 其他自定义错误码
     */
    private Integer code;
    
    /**
     * 响应消息
     * 用于描述响应状态，如 "success"、"保存成功"、"删除失败" 等
     */
    private String message;
    
    /**
     * 响应数据
     * 泛型类型，可以是任意类型的对象或集合
     */
    private T data;
    
    /**
     * 成功响应（带数据）
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return ApiResponse 对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }
    
    /**
     * 成功响应（带消息和数据）
     * 
     * @param message 响应消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return ApiResponse 对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }
    
    /**
     * 错误响应（带状态码和消息）
     * 
     * @param code 错误状态码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ApiResponse 对象
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
    
    /**
     * 错误响应（默认500状态码）
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return ApiResponse 对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null);
    }
}
