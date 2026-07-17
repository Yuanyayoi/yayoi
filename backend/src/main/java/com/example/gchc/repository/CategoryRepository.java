package com.example.gchc.repository;

import com.example.gchc.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CategoryRepository {

    @Select("SELECT * FROM categories ORDER BY sort_order ASC, created_at DESC")
    List<Category> findAll();

    @Select("SELECT * FROM categories WHERE parent_id IS NULL AND enabled = TRUE ORDER BY sort_order ASC")
    List<Category> findByParentIdIsNullAndEnabledTrueOrderBySortOrderAsc();

    @Select("SELECT * FROM categories WHERE parent_id = #{parentId} AND enabled = TRUE ORDER BY sort_order ASC")
    List<Category> findByParentIdAndEnabledTrueOrderBySortOrderAsc(Long parentId);

    @Select("SELECT * FROM categories WHERE id = #{id}")
    Optional<Category> findById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM categories WHERE id = #{id}")
    boolean existsById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM categories WHERE name = #{name}")
    boolean existsByName(String name);

    @Insert("""
            INSERT INTO categories (name, description, parent_id, sort_order, enabled, created_at, updated_at)
            VALUES (#{name}, #{description}, #{parentId}, #{sortOrder}, #{enabled}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);

    @Update("""
            UPDATE categories
            SET name = #{name},
                description = #{description},
                parent_id = #{parentId},
                sort_order = #{sortOrder},
                enabled = #{enabled},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(Category category);

    @Delete("DELETE FROM categories WHERE id = #{id}")
    int deleteById(Long id);

    default Category save(Category category) {
        if (category.getId() == null) {
            insert(category);
        } else {
            update(category);
        }
        return findById(category.getId()).orElse(category);
    }
}
