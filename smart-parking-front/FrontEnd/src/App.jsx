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
import Admin from './pages/Admin/Admin';

function App() {
  const { user } = useContext(AuthContext);
  const location = useLocation(); 

  // Função para verificar se a rota requer autenticação
  const requireAuth = (element) => {
    return user ? element : <Navigate to="/login" />;
  };

  // Redireciona para a tela de login, exceto para a rota de esquecimento de senha
  if (!user && location.pathname !== '/forgot-password') {
    return <UserLog />;
  }

  return (
    <div>
      {/* Exibe o Nav apenas se o usuário logado não for admin */}
      {user && user.role !== "ADMIN" && <Nav />} 
      <Routes>
        <Route path="/home" element={requireAuth(<Home />)} />
        <Route path="/pagar" element={requireAuth(<Payment />)} />
        <Route path="/historico" element={requireAuth(<History />)} />
        <Route path="/perfil" element={requireAuth(<User />)} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/admin" element={requireAuth(<Admin />)} />
        <Route path="/" element={<Navigate to="/home" />} /> 
      </Routes>
    </div>
  );
}

export default App;
