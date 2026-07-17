package com.example.gchc.service;

import com.example.gchc.dto.AwardDTO;
import com.example.gchc.entity.Award;
import com.example.gchc.repository.AwardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwardService {

    private final AwardRepository awardRepository;

    public AwardService(AwardRepository awardRepository) {
        this.awardRepository = awardRepository;
    }

    public List<AwardDTO> getAllAwards() {
        return awardRepository.findAllEnabledOrderBySortOrderAscCreatedAtDesc()
                .stream()
                .map(AwardDTO::fromEntity)
                .toList();
    }

    public AwardDTO getAwardById(Long id) {
        Award award = awardRepository.findById(id);
        return award != null ? AwardDTO.fromEntity(award) : null;
    }

    public AwardDTO createAward(AwardDTO dto) {
        Award award = new Award();
        copyDtoToEntity(dto, award);
        awardRepository.insert(award);
        return AwardDTO.fromEntity(award);
    }

    public AwardDTO updateAward(Long id, AwardDTO dto) {
        Award award = awardRepository.findById(id);
        if (award == null) return null;
        copyDtoToEntity(dto, award);
        award.setId(id);
        awardRepository.update(award);
        return AwardDTO.fromEntity(award);
    }

    public boolean deleteAward(Long id) {
        return awardRepository.deleteById(id) > 0;
    }

    private void copyDtoToEntity(AwardDTO dto, Award award) {
        award.setTitle(dto.getTitle());
        award.setOrganization(dto.getOrganization());
        award.setYear(dto.getYear());
        award.setDescription(dto.getDescription());
        award.setImage(dto.getImage());
        award.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        award.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);
    }
}
