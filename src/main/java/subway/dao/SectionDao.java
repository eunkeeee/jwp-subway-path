package subway.dao;

import subway.entity.SectionEntity;

import java.util.List;

public interface SectionDao {
    SectionEntity save(SectionEntity sectionEntity);

    List<SectionEntity> findAll();

    void delete(Long lineId, Long upStationId, Long downStationId);
}