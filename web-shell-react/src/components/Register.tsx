import {useState} from 'react'
import style from '../styles/login.module.css'
import Button from './Button'
import Input from './Input'
import {registerEndpoint,loginEndpoint} from "../utils/endpoints"
import { useNavigate } from 'react-router-dom'
import {storeToken} from '../utils/tokenUtils'

interface registerValid {
    username : string,
    emial:string,
    password:string,
    confirmPassword: string,
}

interface responseData{
  jwt:string,
  id:number,
  username:string,
  roles:Array<string>
}



export default function Register() {

  const navigate = useNavigate();

  const [credentials, setCredentials] = useState<registerValid>({
    username : "",
    emial:"",
    password:"",
    confirmPassword: "",
    });


  const [badUsername,setBadUsername] = useState(false);

  const [badPassword,setBadPassword] = useState(false);


  const [badEmail,setBadEmail] = useState(false);

  const [passwordDoNotMach,setPasswordDoNotMach] = useState(false);

  const [usernameAlreadytaken,setUsernameAlreadytaken] = useState(false);

  const handleEnter = (event:any) =>{
    if(event.keyCode==13){
      register();
    }
  
  }

  const clearValidation = ()=>{
    setTimeout(()=>{
      setBadPassword(false)
      setBadUsername(false)
      setPasswordDoNotMach(false)
      setBadEmail(false)
      setUsernameAlreadytaken(false)
    },3000);
  }

  const validate = ():Boolean =>{


    let valid = true;

    if(credentials.username==""){
      setBadUsername(true)
      valid =  false
    }

    if(credentials.password==""){
      setBadPassword(true)
      valid = false
    }

    if(credentials.emial==""){
      setBadEmail(true)
      valid = false
    }

    if(credentials.confirmPassword==""){
      setPasswordDoNotMach(true)
      valid = false
    }

    if(credentials.confirmPassword !== credentials.password){
      setPasswordDoNotMach(true)
      setBadPassword(true)
      valid = false
    }

    return valid;


  }


  interface response{
    message:string
  }

  const register = () =>{


  const registerBody = {
    username: credentials.username,
    password: credentials.password
  }

      if(validate()){
        fetch(registerEndpoint, {method:"POST",
        body:JSON.stringify(registerBody),
        headers:{
          'content-type': 'application/json;charset=UTF-8',
        }
      }).then((res)=>res.json())
      .then((data:response)=>{
        if(data.message.includes("Error")){
          setBadUsername(true)
        }else{
          fetch(loginEndpoint,{
            method:"POST",
            body: JSON.stringify(registerBody),
            headers:{
              'content-type': 'application/json;charset=UTF-8',
            }
          })
          .then((res)=>{ return res.json(); })
          .then((data:responseData)=>{ 
            navigate("/dashboard")
            storeToken(data.jwt)
          })
        }
      })
      }
      clearValidation()
  }


  return (
    <div onKeyDown={(event)=> {handleEnter(event)}} className={style.container}>
      <div className={style.card}>
        <h1>Register</h1>
        <div className={`${style.input} ${badUsername? style.inputError:""}`}>
            <Input name="username" type= "text" change={(data:string)=>{credentials.username = data; setCredentials(credentials)}}></Input>
        </div>

        <div className={`${style.input} ${badEmail? style.inputError:""}`}>
            <Input name="email" type= "emial" change={(data:string)=>{credentials.emial = data; setCredentials(credentials)}}></Input>
        </div>

        <div className={`${style.input} ${badPassword? style.inputError:""}`}>
            <Input name="password" type= "password" change={(data:string)=>{credentials.password = data; setCredentials(credentials)}}></Input>
        </div>
        <div className={`${style.input} ${passwordDoNotMach? style.inputError:""}`}>
            <Input name="confitm password" type= "password" change={(data:string)=>{credentials.confirmPassword = data; setCredentials(credentials)}}></Input>
        </div>

        <div className={style.login}>
          <Button name = "Register" action = {register}></Button>
        </div>
      </div>
    </div>
  )
}
