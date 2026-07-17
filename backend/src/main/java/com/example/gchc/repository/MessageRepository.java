package com.example.gchc.repository;

import com.example.gchc.entity.Message;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MessageRepository {

    @Select("SELECT * FROM messages ORDER BY created_at DESC")
    List<Message> findAll();

    @Select("<script>" +
            "SELECT * FROM messages " +
            "<where>" +
            "  <if test='status != null and status != \"\" and !\"all\".equals(status)'>" +
            "    <choose>" +
            "      <when test='\"unreplied\".equals(status)'>replied = false</when>" +
            "      <when test='\"replied\".equals(status)'>replied = true</when>" +
            "    </choose>" +
            "  </if>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (name LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR phone LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR email LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR message LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "</where> " +
            "ORDER BY created_at DESC" +
            "</script>")
    List<Message> findByCondition(@Param("status") String status, @Param("keyword") String keyword);

    @Select("<script>" +
            "SELECT COUNT(*) FROM messages " +
            "<where>" +
            "  <if test='status != null and status != \"\" and !\"all\".equals(status)'>" +
            "    <choose>" +
            "      <when test='\"unreplied\".equals(status)'>replied = false</when>" +
            "      <when test='\"replied\".equals(status)'>replied = true</when>" +
            "    </choose>" +
            "  </if>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (name LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR phone LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR email LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR message LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "</where>" +
            "</script>")
    long countByCondition(@Param("status") String status, @Param("keyword") String keyword);

    @Select("SELECT * FROM messages WHERE id = #{id}")
    Optional<Message> findById(Long id);

    @Select("SELECT COUNT(*) FROM messages")
    long count();

    @Insert("INSERT INTO messages (name, email, phone, message, replied, created_at, updated_at) " +
            "VALUES (#{name}, #{email}, #{phone}, #{message}, false, NOW(6), NOW(6))")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Message message);

    @Update("UPDATE messages SET replied = true, updated_at = NOW(6) WHERE id = #{id}")
    int markAsReplied(Long id);

    @Delete("DELETE FROM messages WHERE id = #{id}")
    int deleteById(Long id);
}
