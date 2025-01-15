package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.Translation;
import com.zubrilka.VideoManager.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video,UUID> {
    Video findByUuid(UUID uuid);

    //чтобы не выгружать весь обьект видео просто найдем его uuid если он есть
    @Query(value = "SELECT v.uuid FROM video v WHERE v.video_info_uuid = :videoInfoUuid LIMIT 1", nativeQuery = true)
    UUID findUuidByVideoInfoUuidIfExists(@Param("videoInfoUuid") UUID videoInfoUuid);

    Optional<Video> findByVideoInfoUuid(UUID videoInfoUuid);
}
