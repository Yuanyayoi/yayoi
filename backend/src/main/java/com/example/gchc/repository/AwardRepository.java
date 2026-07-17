package com.example.gchc.repository;

import com.example.gchc.entity.Award;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AwardRepository {

    @Select("SELECT * FROM awards WHERE enabled = TRUE ORDER BY sort_order ASC, created_at DESC")
    List<Award> findAllEnabledOrderBySortOrderAscCreatedAtDesc();

    @Select("SELECT * FROM awards WHERE id = #{id}")
    Award findById(Long id);

    @Insert("""
            INSERT INTO awards
                (title, organization, year, description, image, sort_order, enabled, created_at, updated_at)
            VALUES
                (#{title}, #{organization}, #{year}, #{description}, #{image}, #{sortOrder}, #{enabled}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Award award);

    @Update("""
            UPDATE awards
            SET title = #{title},
                organization = #{organization},
                year = #{year},
                description = #{description},
                image = #{image},
                sort_order = #{sortOrder},
                enabled = #{enabled},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(Award award);

    @Delete("DELETE FROM awards WHERE id = #{id}")
    int deleteById(Long id);
}
