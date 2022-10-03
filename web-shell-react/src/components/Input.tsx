import syles from "../styles/input.module.css"

interface props{
  name:string;
  type: string;
  change:Function;
  value?: string;
}

export default function Input(props:props) {
  return (
    <input defaultValue={props.value} className={syles.input} onChange={(event)=>props.change(event.target.value)}  placeholder={props.name} type={props.type}></input>
  )
}
