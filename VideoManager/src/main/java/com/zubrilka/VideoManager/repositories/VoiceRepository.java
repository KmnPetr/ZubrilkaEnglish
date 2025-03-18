package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoiceRepository extends JpaRepository<Voice, UUID> {

    @Query(value = """
        SELECT * FROM voice
        WHERE similarity(text, :text) > 0.5
        ORDER BY similarity(text, :text) DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Voice> findSimilarVoices(@Param("text") String text, @Param("limit") int limit);


    @Query(value = "SELECT * FROM voice v WHERE v.uuid IN :uuids", nativeQuery = true)
    List<Voice> findVoicesByUuidsIn(@Param("uuids") List<UUID> uuids);
}
