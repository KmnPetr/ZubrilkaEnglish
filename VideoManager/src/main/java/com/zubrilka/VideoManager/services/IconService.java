package com.zubrilka.VideoManager.services;

import com.zubrilka.VideoManager.models.Icon;
import com.zubrilka.VideoManager.repositories.IconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class IconService {
    private final IconRepository repository;
    @Autowired
    public IconService(IconRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveOrUpdateIcon(Icon icon, UUID videoInfoUuid){
        UUID iconUuid = repository.findUuidByVideoInfoUuidIfExists(videoInfoUuid);

        icon.setVideoInfoUuid(videoInfoUuid);

        if (iconUuid!=null) icon.setUuid(iconUuid);//если ранее icon уже был создан, то просто обновим

        repository.save(icon);
    }

    public Optional<Icon> findIconByVideoInfoUuid(UUID videoInfoUuid) {
        return repository.findByVideoInfoUuid(videoInfoUuid);
    }
}
