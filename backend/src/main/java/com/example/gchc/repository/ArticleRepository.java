package com.example.gchc.repository;

import com.example.gchc.entity.Article;
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
public interface ArticleRepository {

    @Select("SELECT * FROM articles ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Article> findAllPage(@Param("offset") long offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM articles")
    long countAll();

    @Select("SELECT * FROM articles WHERE published = TRUE ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Article> findPublishedPage(@Param("offset") long offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM articles WHERE published = TRUE")
    long countPublished();

    @Select("SELECT * FROM articles WHERE category_id = #{categoryId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Article> findByCategoryIdPage(@Param("categoryId") Long categoryId,
                                       @Param("offset") long offset,
                                       @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM articles WHERE category_id = #{categoryId}")
    long countByCategoryId(Long categoryId);

    @Select("SELECT * FROM articles WHERE published = TRUE AND category_id = #{categoryId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<Article> findPublishedByCategoryIdPage(@Param("categoryId") Long categoryId,
                                                 @Param("offset") long offset,
                                                 @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM articles WHERE published = TRUE AND category_id = #{categoryId}")
    long countPublishedByCategoryId(Long categoryId);

    @Select("SELECT * FROM articles WHERE published = TRUE ORDER BY created_at DESC LIMIT 5")
    List<Article> findTop5ByPublishedTrueOrderByCreatedAtDesc();

    @Select("SELECT * FROM articles WHERE published = TRUE AND show_on_home = TRUE ORDER BY created_at DESC LIMIT 3")
    List<Article> findHomeArticles();

    @Select("SELECT * FROM articles WHERE id = #{id}")
    Optional<Article> findById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM articles WHERE id = #{id}")
    boolean existsById(Long id);

    @Insert("""
            INSERT INTO articles
                (title, content, summary, author, cover_image, category_id, views, published, show_on_home, created_at, updated_at)
            VALUES
                (#{title}, #{content}, #{summary}, #{author}, #{coverImage}, #{categoryId}, #{views}, #{published}, #{showOnHome}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Article article);

    @Update("""
            UPDATE articles
            SET title = #{title},
                content = #{content},
                summary = #{summary},
                author = #{author},
                cover_image = #{coverImage},
                category_id = #{categoryId},
                views = #{views},
                published = #{published},
                show_on_home = #{showOnHome},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(Article article);

    @Delete("DELETE FROM articles WHERE id = #{id}")
    int deleteById(Long id);

    default Article save(Article article) {
        if (article.getViews() == null) {
            article.setViews(0);
        }
        if (article.getPublished() == null) {
            article.setPublished(false);
        }
        if (article.getId() == null) {
            insert(article);
        } else {
            update(article);
        }
        return findById(article.getId()).orElse(article);
    }
}
