import React, { useContext, useEffect } from 'react';
import { AuthContext } from '../../context/AuthProvider';
import Header from '../../components/Header/Header';
import UserAdm from '../../components/UserAdm/UserAdm';

import './Admin.css'

const Admin = () => {
  return (
    <div className='admin'>
      <Header />
      <div className="admin-container">
        <UserAdm />
      </div> 
    </div>
  );
};

export default Admin;
