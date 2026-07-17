package com.example.gchc.repository;

import com.example.gchc.entity.Carousel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CarouselRepository {

    @Select("SELECT * FROM carousels ORDER BY created_at DESC")
    List<Carousel> findAllByOrderByCreatedAtDesc();

    @Select("SELECT * FROM carousels WHERE id = #{id}")
    Optional<Carousel> findById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM carousels WHERE id = #{id}")
    boolean existsById(Long id);

    @Insert("""
            INSERT INTO carousels (title, subtitle, description, image_url, created_at, updated_at)
            VALUES (#{title}, #{subtitle}, #{description}, #{imageUrl}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Carousel carousel);

    @Update("""
            UPDATE carousels
            SET title = #{title},
                subtitle = #{subtitle},
                description = #{description},
                image_url = #{imageUrl},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(Carousel carousel);

    @Delete("DELETE FROM carousels WHERE id = #{id}")
    int deleteById(Long id);

    default Carousel save(Carousel carousel) {
        if (carousel.getId() == null) {
            insert(carousel);
        } else {
            update(carousel);
        }
        return findById(carousel.getId()).orElse(carousel);
    }
}
