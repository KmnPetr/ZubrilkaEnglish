import React from "react";
import { FiCheck } from "react-icons/fi";
import { FiX } from "react-icons/fi";

/**
 * a dialog box pops up when trying to delete a phrase asking to confirm the action
 */
const DeleteDialog = ({onDeletePhrase,onCancel}) => {

    return (
                <div className="overlay">
                    <div className="dialog">
                        <p>Are you sure you want to delete this phrase?</p>
                        <div style={{margin:'5px',display: 'flex', justifyContent:'center'}}>
                            <FiX onClick={onCancel} style={{marginRight:'20px'}} className='clickable'/>
                            <FiCheck onClick={onDeletePhrase} className='clickable'/>
                        </div>
                    </div>
                </div>
    );
};

export default DeleteDialog;