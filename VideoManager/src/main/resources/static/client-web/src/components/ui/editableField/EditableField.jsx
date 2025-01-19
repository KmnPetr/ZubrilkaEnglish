import { useState } from "react";
import './editableField.css';
import { FiCheck, FiX } from "react-icons/fi";
import { TfiMarkerAlt } from "react-icons/tfi";

const EditableField = ({ str, onChange, isStrict, showMarker=false }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [newValue, setValue] = useState(str);

    const handleBlur = () => {
        if (!isStrict) applyChanges();
    };

    const handleInputChange = (e) => setValue(e.target.value);

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            applyChanges(); // Применить изменения
        } else if (e.key === 'Escape') {
            rollback(); // Отменить изменения и выйти из режима редактирования
        }
    };
    

    const applyChanges = () => {
        const nValue = newValue //правка небольшого бага
        setValue(str)
        setIsEditing(false);
        if (nValue !== str) onChange(nValue);
    };

    const rollback = () => {
        setIsEditing(false);
        setValue(str); // Возврат к исходному значению
    };

    return (
        <div className="editable_field">
            {isEditing ? (
                <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                    <input
                        type="text"
                        value={newValue}
                        onChange={handleInputChange}
                        onBlur={handleBlur}
                        autoFocus
                        onKeyDown={handleKeyDown}
                    />
                    {isStrict && (
                        <div>
                            <FiX onClick={rollback} className="clickable" style={{ marginLeft: '10px' }} />
                            <FiCheck onClick={applyChanges} className="clickable" />
                        </div>
                    )}
                </div>
            ) : (
                <div style={{ display: 'flex', alignItems: 'center',gap:'5px' }}>
                    <p onClick={() => setIsEditing(true)}>{str}</p>
                    {showMarker && (<TfiMarkerAlt onClick={()=>setIsEditing(true)} className="clickable"/>)}
                </div>
            )}
        </div>
    );
};

export default EditableField;
