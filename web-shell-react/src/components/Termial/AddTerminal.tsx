import {useEffect, useState} from 'react'
import style from "../../styles/addTerminal.module.css"
import { getAllClients } from '../../utils/endpoints'
import { getToken } from '../../utils/tokenUtils'
import Button from '../Button'
import Input from '../Input'
import { SSHClient } from '../SSHClient'



interface props{
  addTerminalFunction:Function
  currentItem:number|null
}

export default function AddTerminal(props:props) {

  const [sshClient,setSSHClient] = useState<SSHClient>({
    id:null,

    name:"",

    host:"",

    user:"",

    port:"",

    password:"",

    privateKey:null
  })

  useEffect(()=>{
    const requestHeaders: HeadersInit = new Headers();
    requestHeaders.set('Authorization', `Bearer ${getToken()}`);
    if(props.currentItem!=null){
    fetch(`${getAllClients}/one/${props.currentItem}`,{headers:requestHeaders})
    .then(res=>res.json()).then((response:SSHClient)=>setSSHClient(response))
    }
  },[])


  const save = () =>{
    const requestHeaders: HeadersInit = new Headers();
    requestHeaders.set('Content-Type', 'application/json');
    requestHeaders.set('Authorization', `Bearer ${getToken()}`);
    fetch(getAllClients,{
      method:"POST",
      body:JSON.stringify(sshClient),
      headers:requestHeaders
    }).then((res)=>res.json()).then(response=>console.log(response))
      window.location.reload()
  }

  return (

    <div className={style.addTerminalItemCard}>

      <div className={style.editTermial}>
      Edit/Add Terminal
      
      </div>
    
      <div className={style.terminalAddinput}>
        <Input value={String(sshClient.name!)} name='Name' type='text' change={(value:string)=>{sshClient.name=value; setSSHClient(sshClient)}}></Input>
      </div>
      <div className={style.terminalAddinput}>
        <Input value={String(sshClient.host!)}  name='Host' type='text' change={(value:string)=>{sshClient.host=value; setSSHClient(sshClient)}}></Input>
      </div>
      <div className={style.terminalAddinput}>
        <Input value={String(sshClient.user!)} name='User' type='text' change={(value:string)=>{sshClient.user=value; setSSHClient(sshClient)}}></Input>
      </div>
      <div className={style.terminalAddinput}>
        <Input value={String(sshClient.port!)} name='Port' type='text' change={(value:string)=>{sshClient.port=value; setSSHClient(sshClient)}}></Input>
      </div>
      <div className={style.terminalAddinput}>
        <Input value={String(sshClient.password!)} name='Password' type='password' change={(value:string)=>{sshClient.password=value; setSSHClient(sshClient)}}></Input>
      </div>
      <div className={style.terminalAddinput}>
        <Input  name='Private Key' type='file' change={(value:string)=>{sshClient.privateKey=value; setSSHClient(sshClient)}}></Input>
      </div>
    <div className={style.buttons}>
      <div className={style.saveButton}>
        <Button name='save' action={()=>{save()}}></Button>
      </div>
      <div className={style.saveButton}>
        <Button name='close' action={()=>{props.addTerminalFunction()}}></Button>
      </div>
      </div>
    </div>

  )
}
