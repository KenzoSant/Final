import React, { useContext } from 'react';
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Home from './pages/Home/Home';
import Payment from './pages/Payment/Payment';
import History from './pages/History/History';
import User from './pages/User/User';
import Nav from './components/Nav/Nav';
import { AuthContext } from './context/AuthProvider'; 
import UserLog from './components/UserLog/UserLog'; 
import ForgotPassword from './components/ForgotPassword/ForgotPassword';

function App() {
  const { user } = useContext(AuthContext);
  const location = useLocation(); 

  // Se o usuário não estiver logado e a rota atual não for "/forgot-password", exibe a tela de login
  if (!user && location.pathname !== '/forgot-password') {
    return <UserLog />;
  }

  return (
    <div>
      <Routes>
        <Route path="/home" element={<Home />} />
        <Route path="/pagar" element={<Payment />} />
        <Route path="/historico" element={<History />} />
        <Route path="/perfil" element={<User />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/" element={<Navigate to="/home" />} /> 
      </Routes>
      {user && <Nav />} 
    </div>
  );
}

export default App;
