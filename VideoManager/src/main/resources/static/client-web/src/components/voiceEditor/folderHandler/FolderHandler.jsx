import React,{useEffect, useState} from "react";
import './folderHandler.css'
import { LuFolderOpen } from "react-icons/lu";
import { FFmpeg } from "@ffmpeg/ffmpeg";


const ffmpeg = new FFmpeg();


const FolderHandler=({onSelectAudio})=>{
    
  const [audioURL, setAudioURL] = useState(null);

  useEffect(()=>{console.log(audioURL)},[audioURL])

  const openFolderAndGetAudio = async () => {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "audio/*";
    input.click();

    input.onchange = async (event) => {
      const file = event.target.files[0];
      if (!file) return;

      const url = URL.createObjectURL(file);
      setAudioURL(url);
    };
  };

    return(
        <div className="folder_box">
            <LuFolderOpen className='clickable folder' onClick={openFolderAndGetAudio}/>
            {audioURL && <audio controls src={audioURL}></audio>}
        </div>
    )
}
export default FolderHandler