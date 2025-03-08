package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoiceRepository extends JpaRepository<Voice, UUID> {
}
