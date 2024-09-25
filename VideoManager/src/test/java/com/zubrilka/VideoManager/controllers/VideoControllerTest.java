package com.zubrilka.VideoManager.controllers;

import com.zubrilka.VideoManager.models.Video;
import com.zubrilka.VideoManager.services.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class VideoControllerTest {


    @Mock
    private VideoService videoService;

    @InjectMocks
    private VideoController videoController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(videoController).build();
    }

    @Test
    void uploadNewVideo() {
    }

    @Test
    void getVideoById() throws Exception {

        // Создаем UUID
        UUID uuid = UUID.randomUUID();

        // Создаем фейковое видео
        Video video = new Video();
        video.setUuid(uuid);
        video.setFileName("test.mp4");
        video.setBytes(new byte[]{1, 2, 3, 4});

        // Мокируем поведение videoService
        when(videoService.getVideoByUUID(any(UUID.class))).thenReturn(video);

        // Выполняем GET-запрос и проверяем результаты
        mockMvc.perform(get("/video/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType("video/mp4"))
                .andExpect(header().string("UUID", uuid.toString()))
                .andExpect(header().string("X-Filename", "test.mp4"))
                .andExpect(content().bytes(video.getBytes()));
    }

    @Test
    void testGetVideoById_VideoNotFound() throws Exception {
        // Создаем UUID
        UUID uuid = UUID.randomUUID();

        // Мокируем поведение videoService (возвращаем null для случая, когда видео не найдено)
        when(videoService.getVideoByUUID(any(UUID.class))).thenReturn(null);

        // Выполняем GET-запрос и проверяем результаты
        mockMvc.perform(get("/" + uuid))
                .andExpect(status().isNotFound());
    }
    @Test
    void getListVideo() {
    }
}