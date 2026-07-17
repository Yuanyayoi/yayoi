package com.example.gchc.dto;

import com.example.gchc.entity.Award;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwardDTO {
    private Long id;
    private String title;
    private String organization;
    private String year;
    private String description;
    private String image;
    private Integer sortOrder;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AwardDTO fromEntity(Award award) {
        AwardDTO dto = new AwardDTO();
        dto.setId(award.getId());
        dto.setTitle(award.getTitle());
        dto.setOrganization(award.getOrganization());
        dto.setYear(award.getYear());
        dto.setDescription(award.getDescription());
        dto.setImage(award.getImage());
        dto.setSortOrder(award.getSortOrder());
        dto.setEnabled(award.getEnabled());
        dto.setCreatedAt(award.getCreatedAt());
        dto.setUpdatedAt(award.getUpdatedAt());
        return dto;
    }
}
