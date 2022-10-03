import {useState,useImperativeHandle, useEffect, forwardRef,Ref} from 'react'
import style from "../../styles/terminal.module.css"
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { disconnect } from 'process';
import { SSHClient } from '../SSHClient';
import { getToken } from '../../utils/tokenUtils';
import { ssh } from '../../utils/endpoints';

export interface TerminalRef{
  connect: (id:number)=>void;
  
}

interface props{
}

const Terminal = forwardRef((props:props,ref:Ref<TerminalRef>) =>{

    const [tab,setTab] = useState([""])
    const [lines,setLines] = useState([""])
    const [terminal,setTerminal] = useState("")

    useImperativeHandle(ref, () => ({
      connect(id:number) {
       connect(id)
      }
    }));



    const connect = async (id:number)=>{
   
      if(id!==null && id!==undefined){
      await fetchEventSource(ssh+'/subscribe', {
      headers:{
        "sshId": String(id),
        'Authorization':`Bearer ${getToken()}`,
        'Content-Type': 'application/json'
      },
        onmessage(data:any) {
      tab.push(String.fromCharCode(data.data));
      setTab(tab);
      setLines("".concat(...tab).split("\r\n"))
      var scroll:any=document.getElementById("scroll");
      scroll.scrollTop = scroll.scrollHeight;
  }
});
}
  }


  const unconnect = () =>{
    const requestHeaders: HeadersInit = new Headers();
    requestHeaders.set("Authorization",`Bearer ${getToken()}`)
    fetch(ssh,{
      method: "DELETE",
      headers:requestHeaders

    }).then((res)=>res.json()).then(res=>{
      setLines([])
    })

  }
  
  const submit = (commnad:string) =>{
    const requestHeaders: HeadersInit = new Headers();
    requestHeaders.set('Content-Type', 'application/json');
    requestHeaders.set("Authorization",`Bearer ${getToken()}`)
    requestHeaders.set('command',commnad)
    fetch(ssh,{
      method: "POST",
      headers:requestHeaders
    }).then((res)=>res.json()).then(res=>console.log(res))

  }

  const handleSubmit = (key:number)=>{
    if(key===13){
      if(terminal.length===0){
        submit("+]n")
      }else{
        submit(terminal)
        submit("+]n")
      }
  }
  }
  let counter = 0;
  return (
    <div className={style.container}>
        <div>
        <h2>Terminal</h2>
        <button onClick={()=>unconnect()}>disconnect</button>
        </div>
        <div className={style.content} id="scroll">
    {lines.map(el=>{
        counter++;
        return <div key={counter} className={style.line}>{el} {counter==lines.length ? <input onKeyDown={(key)=>{handleSubmit(key.keyCode)}} onChange={(event)=>{setTerminal(event.target.value)}} className={style.terminal}></input>: ""}</div>
      })}
      </div>

    </div>
  )
})
export default Terminal;