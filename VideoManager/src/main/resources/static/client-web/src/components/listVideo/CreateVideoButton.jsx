import React from "react";
import { AiOutlinePlusCircle } from "react-icons/ai";
import "./listVideo.css"
import {createNewVideo} from "../../api/videoInfoService"

/**
 *  the button for creating a new video
 * send a request to the server to create a new video
 */
const CreateVideoButton = ({ onCreateVideo }) => {

    const handleClick = () => {

        createNewVideo()
        .then(response => {
            onCreateVideo()
        })
        .catch(error => {
            console.error(error)
        });

    };

    return (
        <div className="create-video-button" onClick={handleClick}>
            <AiOutlinePlusCircle />
            <p className="non-selectable">
                 create new video
            </p>
        </div>
    );
};

export default CreateVideoButton;