import './transcription.css';

const Transcription = ({className,card}) =>{
    return(<div className={`transcription_box ${className}`}>
        <p>{card.transcription}</p>
    </div>)
}
export default Transcription;