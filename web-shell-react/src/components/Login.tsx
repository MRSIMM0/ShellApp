import styles from "../styles/login.module.css"
import Input from "./Input"
import Button from "./Button"

import { useState } from "react"
import { Interface } from "readline"
import { useNavigate } from "react-router-dom"
import {storeToken} from "../utils/tokenUtils"
import { syncBuiltinESMExports } from "module"

import {loginEndpoint} from "../utils/endpoints"

interface loginForm{
  username:string,
  password:string
}

interface responseData{
  token:string,
}



export default function Login() {

  const [credentials,setCredentials] = useState<loginForm>({username:"",password:""})



  const [badCredential,setBadCreadentials] = useState(false);

  const [badUsername,setBadUsername] = useState(false);

  const [badPassword,setBadPassword] = useState(false);



  let navigate = useNavigate();



  const validate = ():Boolean => {
      let valid = true;

      if(credentials.username==""){
        setBadUsername(true)
        valid = valid && false
      }

      if(credentials.password==""){
        setBadPassword(true)
        valid = valid && false
      }

      return valid;
  }

  const clearValidation = ()=>{
    setTimeout(()=>{
      setBadCreadentials(false)
      setBadPassword(false)
      setBadUsername(false)
    },3000);
  }


const login = () =>{
  if(validate()){
  fetch(loginEndpoint,{
    method:"POST",
    body: JSON.stringify(credentials),
    headers:{
      'content-type': 'application/json;charset=UTF-8',
    }
  })
  .then((res)=>{ return res.json(); })
  .then((data:responseData)=>{ 
    navigate("/dashboard")
    storeToken(data.token)
  }).catch((err)=>{
   setBadCreadentials(true)
  })
}
  clearValidation()
}

const handleEnter = (event:any) =>{
  if(event.keyCode==13){
    login();
  }

}



  return (
    <div onKeyDown={(event)=>{handleEnter(event)}} className={styles.container}>
      <div className={styles.card}>
      <h1>Login</h1>

        <div className={`${styles.input} ${badUsername ? styles.inputError : ""}`}><Input 
        change={
          (data:string)=>{
            credentials.username = data;
            setCredentials(credentials)
          }
          }  name="Login" type="text" /></div>
        <div  className={`${styles.input} ${badPassword ? styles.inputError : ""}`}>
          <Input 
        change={
          (data:string)=>{
            credentials.password = data;
            setCredentials(credentials)
           
          }
          }
          name="Password" type="password"/></div>

         { badCredential ? <small className={styles.error}>Bad Credentials</small> : ""} 
      

        <div  className={`${styles.login} `}><Button action={login} name="Login"/></div>
        <small className={styles.register}><Button action={()=>{  navigate("/register")}} name="Register"/></small>
      </div>

 
    </div>
  )
}