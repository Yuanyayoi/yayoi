package com.example.gchc.repository;

import com.example.gchc.entity.Feature;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FeatureRepository {

    @Select("SELECT id, title, description, style, sort_order AS sortOrder, created_at AS createdAt, updated_at AS updatedAt FROM features ORDER BY sort_order ASC, created_at DESC")
    List<Feature> findAllByOrderByCreatedAtDesc();

    @Select("SELECT * FROM features WHERE id = #{id}")
    Optional<Feature> findById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM features WHERE id = #{id}")
    boolean existsById(Long id);

    @Insert("""
            INSERT INTO features (title, description, style, sort_order, created_at, updated_at)
            VALUES (#{title}, #{description}, #{style}, #{sortOrder}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Feature feature);

    @Update("""
            UPDATE features
            SET title = #{title},
                description = #{description},
                style = #{style},
                sort_order = #{sortOrder},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(Feature feature);

    @Delete("DELETE FROM features WHERE id = #{id}")
    int deleteById(Long id);

    default Feature save(Feature feature) {
        if (feature.getId() == null) {
            insert(feature);
        } else {
            update(feature);
        }
        return findById(feature.getId()).orElse(feature);
    }
}
