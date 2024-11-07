import React, { useContext, useEffect } from 'react';
import { AuthContext } from '../../context/AuthProvider';
import Header from '../../components/Header/Header';
import UserPage from '../../components/UserPage/UserPage';

import './User.css';

const User = () => {
  // const { user } = useContext(AuthContext);
  
  // useEffect(() => {
  //   console.log("Estado do usuário:", user);
  // }, [user]); 

  return (
    <div className='user'>
      <Header />
      <div className="user-container">
        <UserPage />
      </div> 
    </div>
  );
};

export default User;
