import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { assets } from '../../assets/js/assets';
import './Nav.css';

const Nav = () => {
  const location = useLocation(); // Hook para obter a localização atual

  return (
    <nav className="nav">
      <ul>
        {/* Aplicando uma classe "active" ao item de navegação com base na rota */}
        <li className={location.pathname === '/' ? 'active' : ''}>
          <Link to="/home"><img src={assets.home} alt="Home" /></Link>
        </li>
        {/* <li className={location.pathname === '/pagar' ? 'active' : ''}>
          <Link to="/pagar"><img src={assets.pagar} alt="Pagar" /></Link>
        </li> */}
        <li className={location.pathname === '/historico' ? 'active' : ''}>
          <Link to="/historico"><img src={assets.historia} alt="Histórico" /></Link>
        </li>
        <li className={location.pathname === '/perfil' ? 'active' : ''}>
          <Link to="/perfil"><img src={assets.user} alt="Perfil" /></Link>
        </li>
      </ul>
    </nav>
  );
};

export default Nav;
