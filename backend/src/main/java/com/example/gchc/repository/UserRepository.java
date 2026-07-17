package com.example.gchc.repository;

import com.example.gchc.entity.User;
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
public interface UserRepository {

    @Select("SELECT * FROM users ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<User> findPage(@Param("offset") long offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM users")
    long countAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> findById(Long id);

    @Select("SELECT * FROM users WHERE username = #{username}")
    Optional<User> findByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    Optional<User> findByEmail(String email);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE id = #{id}")
    boolean existsById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username}")
    boolean existsByUsername(String username);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email}")
    boolean existsByEmail(String email);

    @Insert("""
            INSERT INTO users (username, password, email, phone, avatar, role, enabled, created_at, updated_at)
            VALUES (#{username}, #{password}, #{email}, #{phone}, #{avatar}, #{role}, #{enabled}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("""
            UPDATE users
            SET username = #{username},
                password = #{password},
                email = #{email},
                phone = #{phone},
                avatar = #{avatar},
                role = #{role},
                enabled = #{enabled},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(Long id);

    default List<User> findAll(long offset, int limit) {
        return findPage(offset, limit);
    }

    default User save(User user) {
        if (user.getId() == null) {
            insert(user);
        } else {
            update(user);
        }
        return findById(user.getId()).orElse(user);
    }
}
