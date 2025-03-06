import React,{useEffect, useState} from "react";
import './folderHandler.css'
import { LuFolderOpen } from "react-icons/lu";
import { convertMp3ToWav } from "../../../utils/audioConverter";



const FolderHandler=({onSelectAudio})=>{

  const openFolderAndGetAudio = async () => {
    const input = document.createElement("input");
    input.type = "file";
    input.accept = "audio/*";
    input.click();

    input.onchange = async (event) => {
      const file = event.target.files[0];
      if (!file) return;

      const url = URL.createObjectURL(file);

      if (true/*file.type === "audio/mpeg"*/) {
        console.log("это mp3 файл")
        convertMp3ToWav(url).then(wavUrl=>{
          onSelectAudio(wavUrl)
        })
        
      }
    };
  };

    return(
        <div className="folder_box">
            <LuFolderOpen className='clickable folder' onClick={openFolderAndGetAudio}/>
        </div>
    )
}
export default FolderHandler