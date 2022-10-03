import React from 'react';
import style from '../../styles/terminalList.module.css'
import { GoPlus } from 'react-icons/go';
import TerminalListItem from './TerminalListItem';
import { SSHClient } from '../SSHClient';


interface props{
  addTerminalFunction:Function;
  data:Array<SSHClient>;
  connect:Function;
  edit:Function;
}

export default function TerminalList(props:props ) {





  return (
<div className={style.terminals}>
    <div className={style.heading}>Terminals 
        <div onClick={()=>{props.addTerminalFunction()}} className={style.addButton}><GoPlus /></div>
    </div>
      <div className={style.terminalList}>
        {props.data.map(el=>{
            return <TerminalListItem edit={(id:number)=>props.edit(id)} connect={(id:number)=>props.connect(el.id)} data={el}/>
        })}
      </div>
  </div>
  )
}
