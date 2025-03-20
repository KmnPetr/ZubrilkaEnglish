import React, { useEffect, useState } from "react";
import { FiTrash2 } from "react-icons/fi";
import DeleteVInfDialog from "../deleteVInfoDialog/DeleteVInfDialog.jsx";
import {useDispatch} from "react-redux";
import "../listVideo.css"
import {downloadIcon} from "../../../api/iconService.js"

const VideoInfoItem = ({videoInfo,handleVideoClick,onDelete}) => {
    const dispatch = useDispatch();
    const [isHover, setIsHover] = useState(false);
    const [isDialogVisible, setIsDialogVisible] = useState(false);
    const [imageUrl, setImageUrl] = useState(null);

    // Загрузка изображения при прорисовке компонента
    useEffect(() => {
        downloadIcon(videoInfo.uuid)
        .then(imageUrl=>{setImageUrl(imageUrl)})
        .catch(error => {});
    }, [videoInfo]);

    const handleMouseEnter = () => {
        setIsHover(true);
    };

    const handleMouseLeave = () => {
        setIsHover(false);
        setIsDialogVisible(false);
    };

    const showDialog = () => {
        setIsDialogVisible(true);
    };

    const hideDialog = () => {
        setIsDialogVisible(false);
    };
    const deleteVideoInfo = () => {
        setIsDialogVisible(false);
        onDelete(videoInfo.uuid)
    };

    return (
        <div style={{position: "relative"}}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            className="video-item">
                {isHover && 
                <div className="video-item-header">
                    <p style={{fontSize:'10px'}}>Uuid: {videoInfo.uuid}</p>
                    <FiTrash2 className='clickable' onClick={showDialog}/> {/*кнопка удаления*/}
                </div>}
                
                {imageUrl && <img src={imageUrl} className="icon"/>}
                <div>
                    <li>
                        <h2>{videoInfo.cnName}</h2>
                        <h2>{videoInfo.enName}</h2>
                        <h2>{videoInfo.ruName}</h2>
                        <p>Переводчик: {videoInfo.translator_name}</p>
                        <p>{videoInfo.description}</p>
                    </li>
                </div>
                {isHover && <button className="view-button" onClick={() => handleVideoClick(videoInfo.uuid)}>Edit Video</button> }

            {/* диалоговое окно */}
            {isDialogVisible && (<DeleteVInfDialog onDelete={deleteVideoInfo} onCancel={hideDialog}/>)}
        </div>
    );
};

export default VideoInfoItem;