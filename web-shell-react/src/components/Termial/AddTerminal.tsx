import { unescape } from 'querystring'
import {useEffect, useState} from 'react'
import { TextEncoder } from 'util'
import style from "../../styles/addTerminal.module.css"
import { getAllClients } from '../../utils/endpoints'
import { getToken } from '../../utils/tokenUtils'
import Button from '../Button'
import Input from '../Input'
import { Method, SSHClient } from '../SSHClient'
import {Buffer} from 'buffer';


interface props{
  addTerminalFunction:Function
  currentItem:number|null
}

export default function AddTerminal(props:props) {

  const [authType,setAuthType] = useState<string>("0");

  const [sshClient,setSSHClient] = useState<SSHClient>({
    id:null,

    name:"",

    host:"",

    user:"",

    port:"",

    password:"",

    key:null,

    method: Method.password
  })

  useEffect(()=>{
  
    const requestHeaders: HeadersInit = new Headers();
    requestHeaders.set('Authorization', `Bearer ${getToken()}`);
    if(props.currentItem!=null){
    fetch(`${getAllClients}one/${props.currentItem}`,{headers:requestHeaders})
    .then(res=>res.json())
    .then((response:SSHClient)=>{setSSHClient(response)
      console.log(sshClient)
      if(sshClient.method===Method.password){
        setAuthType("0");
      }else{
        setAuthType("1");
      }
    })
    }
  },[])


  const files = (event:any) =>{
    var fr=new FileReader();

   

    fr.onload = ()=>{
      let buff = Buffer.from(String(fr.result));
    
      sshClient.key = String(  buff.toString('base64'));
      setSSHClient(sshClient)
      console.log(JSON.stringify(sshClient))
      }
    fr.readAsBinaryString(event.target.files[0])
  
    }

  const save = () =>{
    const requestHeaders: HeadersInit = new Headers();
    requestHeaders.set('Content-Type', 'application/json');
    requestHeaders.set('Authorization', `Bearer ${getToken()}`);
    fetch(getAllClients,{
      method:"POST",
      body:JSON.stringify(sshClient),
      headers:requestHeaders
    }).then((res)=>res.text()).then(response=>console.log(response)).catch(err=>{console.log(err)})
      // window.location.reload()
  }

  const changeAuthType = (event:any) => {
     setAuthType(event.target.value)
     if(event.target.value == "0"){
      sshClient.method = Method.password;
     }
     if(event.target.value == "1"){
      sshClient.method = Method.key;
     }
     setSSHClient(sshClient);
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
      <div className={style.radios}>
        <div>
          <label>Password</label>
          <input value="0" name='auth' defaultChecked onChange={(event)=>{changeAuthType(event)}} type="radio"></input>
          </div>
          <div>
          <label>Key</label>
          <input value="1"name='auth' onChange={(event)=>{changeAuthType(event)}} type="radio"></input>
          </div>
        </div>
      <div className={style.authType}>
        {
           authType == "1" ? 
            <div className={style.authTypeInput}>
              <input  type='file' onChange={(event)=>{files(event)}}></input>
            </div>
           :      
            <div className={style.authTypeInput}>
                <Input value={String(sshClient.password!)} name='Password' type='password' change={(value:string)=>{sshClient.password=value; setSSHClient(sshClient)}}></Input>
            </div>
        }
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
