import './listOldVoice.css';

const ListOldVoice=({className,style,str, onSelectOldVoice})=>{
    return (<div className={`${className}`} style={style}>
        <h1>{`ListOldVoice: `+str}</h1>
    </div>)
}
export default ListOldVoice;