package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    Video findByUuid(UUID uuid);
}
