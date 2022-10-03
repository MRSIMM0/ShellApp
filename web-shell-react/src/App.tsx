import React from 'react';
import logo from './logo.svg';
import './App.css';
import Login from './components/Login';
import Dashborad from './components/Dashborad';
import { Route, Routes } from 'react-router-dom';
import Register from './components/Register';
import PageNotFound from './components/PageNotFound';
function App() {
  return (
    <>
    <Routes>
      <Route path='/' element={<Login />}/>
      <Route path='/dashboard' element={<Dashborad/>}/>
      <Route path='/register' element={<Register />}/>
      <Route path='*' element={<PageNotFound/>}/>
    </Routes>
  </>
  );
}

export default App;
