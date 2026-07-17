package com.example.gchc.repository;

import com.example.gchc.entity.Icon;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface IconRepository {

    @Select("SELECT * FROM icons ORDER BY sort_order ASC, created_at DESC")
    List<Icon> findAll();

    @Select("SELECT * FROM icons WHERE enabled = true ORDER BY sort_order ASC, created_at DESC")
    List<Icon> findAllEnabled();

    @Select("SELECT * FROM icons WHERE id = #{id}")
    Optional<Icon> findById(Long id);

    @Select("SELECT * FROM icons WHERE image_url = #{imageUrl}")
    Optional<Icon> findByImageUrl(String imageUrl);

    @Select("SELECT COUNT(*) > 0 FROM icons WHERE image_url = #{imageUrl}")
    boolean existsByImageUrl(String imageUrl);

    @Select("SELECT COUNT(*) > 0 FROM icons WHERE id = #{id}")
    boolean existsById(Long id);

    @Insert("INSERT INTO icons (image_url, name, description, enabled, sort_order, created_at, updated_at) " +
            "VALUES (#{imageUrl}, #{name}, #{description}, #{enabled}, #{sortOrder}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Icon icon);

    @Update("UPDATE icons SET image_url = #{imageUrl}, name = #{name}, description = #{description}, " +
            "enabled = #{enabled}, sort_order = #{sortOrder}, updated_at = #{updatedAt} WHERE id = #{id}")
    void update(Icon icon);

    @Delete("DELETE FROM icons WHERE id = #{id}")
    void deleteById(Long id);

    @Delete("DELETE FROM icons")
    void truncateAll();
}