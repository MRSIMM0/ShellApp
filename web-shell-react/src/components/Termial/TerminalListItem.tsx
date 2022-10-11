import React from 'react'
import style from '../../styles/termialListItem.module.css'
import Button from '../Button'

import {FiTrash,FiEdit2} from 'react-icons/fi'
import {getAllClients} from "../../utils/endpoints"
import {TbPlugConnected} from 'react-icons/tb'
import { SSHClient } from '../SSHClient'
import { getToken } from '../../utils/tokenUtils'


interface props{
    data: SSHClient;
    connect:Function;
    edit:Function
}

export default function TerminalListItem(props:props) {

    const del = (id:number)=>{
       if(id!==null && id !==undefined){
        const requestHeaders: HeadersInit = new Headers();
        requestHeaders.set('Authorization', `Bearer ${getToken()}`);
        fetch(`${getAllClients}${id}`,
        {   
            headers:requestHeaders,
            method:"DELETE"
        }
        )
        .then(res=>window.location.reload())
       }
    }

  return (
    <div className={style.terminalItem}>
        <div className={style.name}>{props.data.name}</div>
        <div className={style.buttons}>
            <div onClick={()=>props.connect(props.data.id)} className={style.button}>
                <TbPlugConnected/>
            </div>
            <div onClick={()=>props.edit(props.data.id)} className={style.button}>
                <FiEdit2/>
            </div>
            <div onClick={()=> del(props.data.id!)} className={style.button}>
                <FiTrash/>
            </div>
        </div>
    </div>
  )
}
