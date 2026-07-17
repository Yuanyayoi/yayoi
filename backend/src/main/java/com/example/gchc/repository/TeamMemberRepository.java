package com.example.gchc.repository;

import com.example.gchc.entity.TeamMember;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TeamMemberRepository {

    @Select("SELECT * FROM team_members ORDER BY sort_order ASC, created_at DESC")
    List<TeamMember> findAllByOrderBySortOrderAscCreatedAtDesc();

    @Select("SELECT * FROM team_members WHERE enabled = TRUE ORDER BY sort_order ASC, created_at DESC")
    List<TeamMember> findByEnabledTrueOrderBySortOrderAscCreatedAtDesc();

    @Select("SELECT * FROM team_members WHERE id = #{id}")
    Optional<TeamMember> findById(Long id);

    @Select("SELECT COUNT(*) > 0 FROM team_members WHERE id = #{id}")
    boolean existsById(Long id);

    @Insert("""
            INSERT INTO team_members (name, position, bio, avatar, sort_order, enabled, created_at, updated_at)
            VALUES (#{name}, #{position}, #{bio}, #{avatar}, #{sortOrder}, #{enabled}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TeamMember member);

    @Update("""
            UPDATE team_members
            SET name = #{name},
                position = #{position},
                bio = #{bio},
                avatar = #{avatar},
                sort_order = #{sortOrder},
                enabled = #{enabled},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(TeamMember member);

    @Delete("DELETE FROM team_members WHERE id = #{id}")
    int deleteById(Long id);

    default TeamMember save(TeamMember member) {
        if (member.getSortOrder() == null) {
            member.setSortOrder(0);
        }
        if (member.getEnabled() == null) {
            member.setEnabled(true);
        }
        if (member.getId() == null) {
            insert(member);
        } else {
            update(member);
        }
        return findById(member.getId()).orElse(member);
    }
}
