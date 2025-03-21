import './translation.css';

const Translation=({className,card})=>{
    return (<div className={`translation_box ${className}`}>
        <p>{JSON.stringify(card.translation)}</p>
    </div>)
}
export default Translation;