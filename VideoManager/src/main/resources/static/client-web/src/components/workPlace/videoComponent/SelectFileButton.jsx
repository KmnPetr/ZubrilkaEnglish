import React, {useRef} from "react";
import { AiOutlineCloudUpload } from "react-icons/ai";
import {useDispatch} from "react-redux";
import {saveVideoPath} from "../../../store/reducers/videoManagementReducer";

/**
 * Функциональный компонент для отображения видео, над которым идет работа
 */
const SelectFileButton = ({onSelectVideo}) => {

    const dispatch = useDispatch();
    // Создание рефа для скрытого input
    const fileInputRef = useRef(null);

    // Функция для инициирования клика на скрытый input
    const handleClick = () => {
        fileInputRef.current.click();
    };


    const handleFileChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            const videoURL = URL.createObjectURL(file);
            // dispatch(saveVideoPath(videoURL))
            onSelectVideo(videoURL);
        }
    };

    return (
        <div className="selectFileButton" onClick={handleClick}>
            <AiOutlineCloudUpload style={{marginRight: '5px'}}/>
            <p className="non-selectable">Upload video</p>
            <input
                type="file"
                accept="video/*"
                ref={fileInputRef}
                onChange={handleFileChange}
                style={{ display: 'none' }} // Скрыть input
            />
        </div>
    );
};

export default SelectFileButton;