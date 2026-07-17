package com.example.gchc.repository;

import com.example.gchc.entity.SiteSetting;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SiteSettingRepository {

    @Select("SELECT * FROM site_settings WHERE group_name = #{groupName} ORDER BY setting_key ASC")
    List<SiteSetting> findByGroupName(String groupName);

    @Select("SELECT * FROM site_settings WHERE group_name = #{groupName} AND setting_key = #{settingKey}")
    Optional<SiteSetting> findByGroupNameAndSettingKey(String groupName, String settingKey);

    @Select("SELECT * FROM site_settings WHERE id = #{id}")
    Optional<SiteSetting> findById(Long id);

    @Insert("""
            INSERT INTO site_settings (group_name, setting_key, setting_value, created_at, updated_at)
            VALUES (#{groupName}, #{settingKey}, #{settingValue}, NOW(6), NOW(6))
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SiteSetting setting);

    @Update("""
            UPDATE site_settings
            SET group_name = #{groupName},
                setting_key = #{settingKey},
                setting_value = #{settingValue},
                updated_at = NOW(6)
            WHERE id = #{id}
            """)
    int update(SiteSetting setting);

    @Delete("DELETE FROM site_settings WHERE id = #{id}")
    int deleteById(Long id);

    default SiteSetting save(SiteSetting setting) {
        if (setting.getId() == null) {
            insert(setting);
        } else {
            update(setting);
        }
        return findById(setting.getId()).orElse(setting);
    }
}
