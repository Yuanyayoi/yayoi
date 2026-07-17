package com.example.gchc.repository;

import com.example.gchc.entity.NewsCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface NewsCategoryRepository {

    @Select("SELECT * FROM news_categories ORDER BY sort_order ASC, id DESC")
    List<NewsCategory> findAll();

    @Select("SELECT * FROM news_categories WHERE enabled = TRUE ORDER BY sort_order ASC, id DESC")
    List<NewsCategory> findAllEnabled();

    @Select("SELECT * FROM news_categories WHERE id = #{id}")
    Optional<NewsCategory> findById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM news_categories WHERE id = #{id}")
    boolean existsById(Long id);

    @Insert("""
            INSERT INTO news_categories (name, description, sort_order, enabled, created_at, updated_at)
            VALUES (#{name}, #{description}, #{sortOrder}, #{enabled}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(NewsCategory category);

    @Update("""
            UPDATE news_categories
            SET name = #{name},
                description = #{description},
                sort_order = #{sortOrder},
                enabled = #{enabled},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(NewsCategory category);

    @Delete("DELETE FROM news_categories WHERE id = #{id}")
    int deleteById(Long id);

    default NewsCategory save(NewsCategory category) {
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getEnabled() == null) {
            category.setEnabled(true);
        }
        if (category.getId() == null) {
            insert(category);
        } else {
            update(category);
        }
        return findById(category.getId()).orElse(category);
    }
}
