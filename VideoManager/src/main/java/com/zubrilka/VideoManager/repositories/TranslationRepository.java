package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, UUID> {

    Optional<Translation> findByVideoInfoUuid(UUID videoInfoUuid);
}
