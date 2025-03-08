package com.zubrilka.VideoManager.util;

import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * дает набор лазличных методов для работу с медиа файлами
 * для работы необходим установленный ffmpeg и доступный через командную строку по команде "ffmpeg"
 */
@Component
public class FfmpegService {


    public void convertWavToMp3(String inputWavPath, String outputMp3Path) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", inputWavPath, "-codec:a", "libmp3lame", "-qscale:a", "2", outputMp3Path
        );
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Ошибка при конвертации WAV в MP3");
        }
    }
}
