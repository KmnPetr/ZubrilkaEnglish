import './listАvailableCard.css';
import {useEffect, useState} from "react";
import {getListCards} from "../../../../../api/cardService.js";
import Card from "./card/Card.jsx";

/**
 * отобразит список уже имеющихся карт схожих по str
 */
const ListАvailableCard = ({className, str}) => {
    const [listCards, setListCards] = useState([])

    useEffect(()=>{
        getListCards({ text:str})
            .then(listVoice=>{
                setListCards(listVoice);
            })
            .catch(err=>{setListCards([])})
    },[str])
    return (<div className={`listAvailableCard_box ${className}`}>
        {listCards && listCards.map(card => (
            <Card key={card.uuid} card={card}/>
        ))}

    </div>)
};

export default ListАvailableCard;