package com.example.gchc.repository;

import com.example.gchc.entity.Product;
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
public interface ProductRepository {

    @Select("""
            SELECT p.* FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            LEFT JOIN categories pc ON c.parent_id = pc.id
            ORDER BY COALESCE(pc.sort_order, c.sort_order) ASC, p.created_at DESC
            LIMIT #{limit} OFFSET #{offset}
            """)
    List<Product> findAllPage(@Param("offset") long offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM products")
    long countAll();

    @Select("""
            SELECT p.* FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            LEFT JOIN categories pc ON c.parent_id = pc.id
            WHERE p.category_id = #{categoryId}
            ORDER BY COALESCE(pc.sort_order, c.sort_order) ASC, p.created_at DESC
            LIMIT #{limit} OFFSET #{offset}
            """)
    List<Product> findByCategoryIdPage(@Param("categoryId") Long categoryId,
                                       @Param("offset") long offset,
                                       @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM products WHERE category_id = #{categoryId}")
    long countByCategoryId(Long categoryId);

    @Select("""
            SELECT p.* FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            LEFT JOIN categories pc ON c.parent_id = pc.id
            WHERE p.enabled = TRUE
            ORDER BY COALESCE(pc.sort_order, c.sort_order) ASC, p.created_at DESC
            LIMIT #{limit} OFFSET #{offset}
            """)
    List<Product> findEnabledPage(@Param("offset") long offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM products WHERE enabled = TRUE")
    long countEnabled();

    @Select("""
            SELECT p.* FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            LEFT JOIN categories pc ON c.parent_id = pc.id
            WHERE p.enabled = TRUE
            ORDER BY COALESCE(pc.sort_order, c.sort_order) ASC, p.created_at DESC
            """)
    List<Product> findAllEnabled();

    @Select("""
            SELECT p.* FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            LEFT JOIN categories pc ON c.parent_id = pc.id
            WHERE p.enabled = TRUE
            ORDER BY COALESCE(pc.sort_order, c.sort_order) ASC, p.created_at DESC
            LIMIT 8
            """)
    List<Product> findTop8ByEnabledTrueOrderByCreatedAtDesc();

    @Select("""
            SELECT p.* FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            LEFT JOIN categories pc ON c.parent_id = pc.id
            WHERE p.enabled = TRUE AND p.show_on_home = TRUE
            ORDER BY COALESCE(pc.sort_order, c.sort_order) ASC, p.created_at DESC
            LIMIT 6
            """)
    List<Product> findHomeProducts();

    @Select("SELECT * FROM products WHERE id = #{id}")
    Optional<Product> findById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM products WHERE id = #{id}")
    boolean existsById(Long id);

    @Insert("""
            INSERT INTO products
                (name, description, image, price, category_id, stock, enabled, show_on_home, created_at, updated_at)
            VALUES
                (#{name}, #{description}, #{image}, #{price}, #{categoryId}, #{stock}, #{enabled}, #{showOnHome}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Update("""
            UPDATE products
            SET name = #{name},
                description = #{description},
                image = #{image},
                price = #{price},
                category_id = #{categoryId},
                stock = #{stock},
                enabled = #{enabled},
                show_on_home = #{showOnHome},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(Product product);

    @Delete("DELETE FROM products WHERE id = #{id}")
    int deleteById(Long id);

    default Product save(Product product) {
        if (product.getStock() == null) {
            product.setStock(0);
        }
        if (product.getEnabled() == null) {
            product.setEnabled(true);
        }
        if (product.getId() == null) {
            insert(product);
        } else {
            update(product);
        }
        return findById(product.getId()).orElse(product);
    }
}
