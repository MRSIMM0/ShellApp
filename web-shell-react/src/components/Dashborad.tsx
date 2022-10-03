import React from 'react';
import { useEffect,useState, createRef} from 'react'
import {getToken,removeToken} from "../utils/tokenUtils"
import Terminal, { TerminalRef } from './Termial/Terminal';
import style from "../styles/dashboard.module.css"
import {FiLogOut} from "react-icons/fi"
import AddTerminal from './Termial/AddTerminal';
import TerminalList from './Termial/TerminalList';
import {getAllClients} from "../utils/endpoints"
import { useNavigate } from 'react-router-dom';

export default function Dashborad() {
  
    const ref = createRef<TerminalRef>();

    const [isAddVisible,setAddVisible] = useState(false);

    const [data,setData] = useState([]);

    const [current,setCurrent] = useState<number | null>(null);
    const navigate = useNavigate()

    const edit = (id:number) =>{
      setCurrent(id)
      toggleTermianl();
    }

    const connect = (id:number)=>{
      ref.current?.connect(id)
    }

    useEffect(()=>{

      const requestHeaders: HeadersInit = new Headers();
      requestHeaders.set('Authorization', `Bearer ${getToken()}`);

      fetch(getAllClients,{
        headers: requestHeaders,
        method:"GET"
      }).then(res=>res.json()).then((response:any)=>{ 
        if(response.status==500){
          navigate("/")
        }
        setData(response)})
    },[])
  
  const toggleTermianl = () =>{
      setAddVisible(!isAddVisible);
      if(isAddVisible){
        setCurrent(null)
      }
  }

  const logout = () =>{
    removeToken();
    navigate("/")
  }

  return (
    <div className={style.container}>
      <div onClick={()=>{logout()}} className={style.logout}>
        <FiLogOut/>
      </div>
      <div className={style.right}>
        <div className={style.terminal}>
        <Terminal ref={ref}></Terminal>
        </div>
      </div>

      <div className={style.left}>
      <TerminalList edit={(id:number)=>{edit(id)}} connect={(id:number)=>connect(id)} data={data} addTerminalFunction={()=>toggleTermianl()} />
      </div>
      {isAddVisible ? 
      <div className={style.addTerminalItem}>
      <AddTerminal currentItem={current} addTerminalFunction={()=>toggleTermianl()} />
      </div>
        :""}
    </div>
   
  )
  
}
