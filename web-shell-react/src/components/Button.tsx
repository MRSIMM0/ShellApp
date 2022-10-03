import React from 'react'
import styles from '../styles/button.module.css'

interface props{
  name:string
  action:Function
}


export default function Button(props:props) {
  return (
    <div onClick={()=>props.action()} className={styles.container}>{props.name}</div>
  )
}
