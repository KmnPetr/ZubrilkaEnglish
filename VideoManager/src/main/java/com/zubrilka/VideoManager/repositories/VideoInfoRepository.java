package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoInfoRepository extends JpaRepository<VideoInfo, UUID> {
    Optional<VideoInfo> findByUuid(UUID uuid);

    @Query(value = "SELECT * FROM video_info WHERE translator_uuid = :translator_uuid", nativeQuery = true)
    List<VideoInfo> findVideosByTranslatorUuid(@Param("translator_uuid") UUID translator_uuid);

}
