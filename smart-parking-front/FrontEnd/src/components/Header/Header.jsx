import React, { useContext } from 'react';
import { AuthContext } from '../../context/AuthProvider'; // Ajuste o caminho
import './Header.css';

const Header = () => {
  const { user } = useContext(AuthContext);

  const today = new Date();
  const options = { weekday: 'short', day: 'numeric', month: 'short', year: 'numeric' };
  const formattedDate = today.toLocaleDateString('pt-BR', options);

  return (
    <header className="header">
      <h2>Olá, <span>{user ? user.name : 'Usuário'}</span>! 👋</h2>
      {/* <p>{formattedDate}</p> */}
    </header>
  );
};

export default Header;
