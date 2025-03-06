export const convertMp3ToWav = async (mp3Url) => {
    try {
      const response = await fetch(mp3Url);
      const arrayBuffer = await response.arrayBuffer();
  
      const audioContext = new (window.AudioContext || window.webkitAudioContext)();
      const audioBuffer = await audioContext.decodeAudioData(arrayBuffer);
  
      const wavBlob = audioBufferToWav(audioBuffer);
      return URL.createObjectURL(wavBlob);
    } catch (error) {
      console.error("Ошибка при конвертации MP3 в WAV:", error);
      return null;
    }
  };
  
  const audioBufferToWav = (audioBuffer) => {
    const numOfChannels = audioBuffer.numberOfChannels;
    const sampleRate = audioBuffer.sampleRate;
    const length = audioBuffer.length * numOfChannels * 2 + 44;
    const buffer = new ArrayBuffer(length);
    const view = new DataView(buffer);
  
    writeWavHeader(view, audioBuffer, numOfChannels, sampleRate);
  
    let offset = 44;
    for (let i = 0; i < audioBuffer.length; i++) {
      for (let channel = 0; channel < numOfChannels; channel++) {
        let sample = audioBuffer.getChannelData(channel)[i] * 32767;
        view.setInt16(offset, sample, true);
        offset += 2;
      }
    }
  
    return new Blob([buffer], { type: "audio/wav" });
  };
  
  const writeWavHeader = (view, audioBuffer, numOfChannels, sampleRate) => {
    const length = audioBuffer.length * numOfChannels * 2 + 44;
    writeString(view, 0, "RIFF");
    view.setUint32(4, length - 8, true);
    writeString(view, 8, "WAVE");
    writeString(view, 12, "fmt ");
    view.setUint32(16, 16, true);
    view.setUint16(20, 1, true);
    view.setUint16(22, numOfChannels, true);
    view.setUint32(24, sampleRate, true);
    view.setUint32(28, sampleRate * numOfChannels * 2, true);
    view.setUint16(32, numOfChannels * 2, true);
    view.setUint16(34, 16, true);
    writeString(view, 36, "data");
    view.setUint32(40, length - 44, true);
  };
  
  const writeString = (view, offset, string) => {
    for (let i = 0; i < string.length; i++) {
      view.setUint8(offset + i, string.charCodeAt(i));
    }
  };