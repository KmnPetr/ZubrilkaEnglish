import './usedCard.css';
import {useEffect, useState} from "react";
import {getCardByUuid} from "../../../../../api/cardService.js";

const UsedCard=({className,style,card_uuid})=>{
    const [card,setCard]=useState(null);

    useEffect(()=>{
        if(card_uuid){
            getCardByUuid(card_uuid)
                .then(card=>setCard(card))
                .catch(e=>console.error(e))
        } else setCard(null)
    },[card_uuid])

    return(<div className={`usedCard ${className}`}>
        <p>{'Used_card_uuid: '+card_uuid}</p>
        <p>{JSON.stringify(card)}</p>
    </div>)
}
export default UsedCard;
