import React, { useState, useContext, useEffect } from 'react';
import { AuthContext } from '../../context/AuthProvider';
import { useNavigate } from 'react-router-dom';  
import styles from '../UserLog/UserLog.module.css'; 

const ForgotPassword = () => {
  const { resetPassword, loading, clearMessages, changePasswordMessages } = useContext(AuthContext);
  const [email, setEmail] = useState('');
  const navigate = useNavigate();  

  useEffect(() => {
    clearMessages('changePassword');
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    resetPassword(email);
  };

  const handleBackToLogin = () => {
    navigate('/');  
  };

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Esqueceu a Senha?</h1>
      <form onSubmit={handleSubmit} className={styles.form}>
        <label className={styles.formGroup}>Digite seu email:</label>
        <input
          type="email"
          id="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className={styles.input}
        />
        <button type="submit" disabled={loading} className={styles.submitButton}>
          {loading ? 'Enviando...' : 'Enviar'}
        </button>
      </form>

      {changePasswordMessages.success && <div className="success">{changePasswordMessages.success}</div>}
      {changePasswordMessages.error && <div className="error">{changePasswordMessages.error}</div>}

      <button className={styles.toggleButton} onClick={handleBackToLogin}>
        Voltar ao Login
      </button>
    </div>
  );
};

export default ForgotPassword;
