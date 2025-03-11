package com.zubrilka.VideoManager.util;

import com.zubrilka.VideoManager.models.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class MediaLocalStorage {

    private final FfmpegService ffmpegService;

    private static final String VOICE_FOLDER = "voice"; //папку надо создать самостоятельно
    private static final String VIDEO_FOLDER = "video"; //папку надо создать самостоятельно
    @Value("${UPLOAD_MEDIA_DIR}")
    private String UPLOAD_MEDIA_DIR;

    public MediaLocalStorage(FfmpegService ffmpegService) {
        this.ffmpegService = ffmpegService;
    }

    public String saveVideo(MultipartFile file, String uuid) {
        String fileName = uuid+".mp4";

        try{
            File videoFile = new File(Paths.get(UPLOAD_MEDIA_DIR, VIDEO_FOLDER,fileName).toString());
            if (!videoFile.createNewFile()) {throw new IOException("Failed to create the file.");}

            if (!file.getContentType().equals("video/mp4")) throw new RuntimeException("Type of video must be a \"video/mp4\". Current type: \""+file.getContentType()+"\"");
            file.transferTo(videoFile);

            return Paths.get(VIDEO_FOLDER, fileName).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String saveWavVoice(MultipartFile file, String uuid) {
        String mp3FileName = uuid + ".mp3";
        String mp3FilePath = Paths.get(UPLOAD_MEDIA_DIR, VOICE_FOLDER, mp3FileName).toString();

        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Файл не загружен!");
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

    public void deleteVideo(String uuid) {
        String videoPath = Paths.get(UPLOAD_MEDIA_DIR, VIDEO_FOLDER, uuid + ".mp4").toString();

        File videoFile = new File(videoPath);

        if (videoFile.exists()) {
            boolean deleted = videoFile.delete();

            if (deleted) {
                System.out.println("File " + videoPath + " was deleted successfully.");
            } else {
                System.out.println("Failed to delete file " + videoPath);
            }
        } else {
            System.out.println("File " + videoPath + " not found.");
        }
    }

    public BufferedInputStream getVideoAsStream(String localPath) {

        // Строим полный путь к видеофайлу
        String fullPath = Paths.get(UPLOAD_MEDIA_DIR, localPath).toString();

        // Открываем файл для потокового чтения с использованием BufferedInputStream
        try {
            return new BufferedInputStream(new FileInputStream(fullPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
