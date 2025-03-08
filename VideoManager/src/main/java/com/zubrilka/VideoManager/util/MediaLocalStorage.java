package com.zubrilka.VideoManager.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class MediaLocalStorage {

    private final FfmpegService ffmpegService;

    private static final String VOICE_FOLDER = "voice";
    @Value("${UPLOAD_MEDIA_DIR}")
    private String UPLOAD_MEDIA_DIR;

    public MediaLocalStorage(FfmpegService ffmpegService) {
        this.ffmpegService = ffmpegService;
    }

    public String saveWavVoice(MultipartFile file, String uuid) {
        String mp3FileName = uuid + ".mp3";
        String mp3FilePath = Paths.get(UPLOAD_MEDIA_DIR, VOICE_FOLDER, mp3FileName).toString();

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Файл не загружен!");
            }

            // Создание папки, если её нет
            File uploadDir = new File(Paths.get(UPLOAD_MEDIA_DIR, VOICE_FOLDER).toString());
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new Exception("Ошибка при создании папки для файлов!");
            }

            // Создаём временный WAV-файл
            File tempWavFile = File.createTempFile("temp_", ".wav");
            file.transferTo(tempWavFile);

            System.err.println(tempWavFile.getAbsolutePath());

            // Конвертируем в MP3 положит готовый файл сразу по указанному пути
            ffmpegService.convertWavToMp3(tempWavFile.getAbsolutePath(), mp3FilePath);

            // Удаляем временный WAV-файл
            Files.deleteIfExists(tempWavFile.toPath());

            return Paths.get(VOICE_FOLDER, mp3FileName).toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка при обработке файла: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
