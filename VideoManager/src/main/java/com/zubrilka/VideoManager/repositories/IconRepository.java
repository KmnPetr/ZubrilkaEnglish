package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.Icon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IconRepository extends JpaRepository<Icon,UUID> {

    //чтобы не выгружать весь обьект icon просто найдем его uuid если он есть
    @Query(value = "SELECT v.uuid FROM icon v WHERE v.video_info_uuid = :videoInfoUuid LIMIT 1", nativeQuery = true)
    UUID findUuidByVideoInfoUuidIfExists(@Param("videoInfoUuid") UUID videoInfoUuid);

    Optional<Icon> findByVideoInfoUuid(UUID videoInfoUuid);
}
