import React, { useContext, useState, useEffect } from 'react';
import { AuthContext } from '../../context/AuthProvider';
import { useNavigate } from 'react-router-dom';
import { FaEye, FaEyeSlash } from 'react-icons/fa';
import EmailVerificationModal from '../EmailVerification/EmailVerification'; 
import styles from './UserLog.module.css';

const UserLog = () => {
  const { login, register, loading, clearMessages, loginMessages, registerMessages } = useContext(AuthContext);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [isLogin, setIsLogin] = useState(true);
  const [showPassword, setShowPassword] = useState(false);
  const [passwordError, setPasswordError] = useState('');
  const [isVerificationModalVisible, setIsVerificationModalVisible] = useState(false);
  const [isEmailSent, setIsEmailSent] = useState(false);
  const navigate = useNavigate();

  const toggleForm = () => {
    setIsLogin(!isLogin);
    setEmail('');
    setPassword('');
    setName('');
  };

  // Validação de senha com regex
  const validatePassword = (pwd) => {
    const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&_-])[A-Za-z\d@$!%*?&_-]{8,}$/;
    return regex.test(pwd);
  };

  const sendVerificationEmail = async (email) => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/email-checker', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
      });
      if (response.ok) {
        setIsEmailSent(true);
      } else {
        throw new Error('Falha ao enviar email de verificação');
      }
    } catch (error) {
      console.error('Erro ao enviar email de verificação:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const trimmedEmail = email.trim();
    const trimmedPassword = password.trim();
  
    if (!isLogin && !validatePassword(trimmedPassword)) {
      setPasswordError('A senha deve ter no mínimo 8 caracteres, incluindo 1 letra maiúscula, 1 número e 1 caractere especial.');
      return;
    }
  
    setPasswordError('');
  
    if (isLogin) {
      login(trimmedEmail, trimmedPassword);
    } else {
      await register(name, trimmedEmail, trimmedPassword);
  
      // Mostra o modal de verificação de e-mail, mas não chama a API novamente
      setIsVerificationModalVisible(true);
      setIsEmailSent(true); // Define que o e-mail foi enviado, sem chamar novamente a API
    }
  };

  useEffect(() => {
    clearMessages(isLogin ? 'login' : 'register');
  }, [isLogin]);

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>{isLogin ? 'Login' : 'Cadastro'}</h1>

      <form className={styles.form} onSubmit={handleSubmit}>
        {!isLogin && (
          <div className={styles.formGroup}>
            <label htmlFor="name">Nome</label>
            <input
              type="text"
              id="name"
              required
              className={styles.input}
              placeholder="Digite seu nome"
              value={name}
              maxLength={50}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
        )}

        <div className={styles.formGroup}>
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            required
            className={styles.input}
            placeholder="Digite seu email"
            value={email}
            maxLength={50}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>

        <div className={styles.formGroup}>
          <label htmlFor="password">Senha</label>
          <div className={styles.passwordContainer}>
            <input
              type={showPassword ? 'text' : 'password'}
              id="password"
              required
              className={styles.input}
              placeholder="Digite sua senha"
              value={password}
              maxLength={100}
              onChange={(e) => setPassword(e.target.value)}
            />
            <button
              type="button"
              className={styles.showPasswordButton}
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? <FaEyeSlash /> : <FaEye />}
            </button>
          </div>
          {!isLogin && passwordError && <p className={styles.error}>{passwordError}</p>}
        </div>

        <button type="submit" className={styles.submitButton} disabled={loading}>
          {loading ? 'Carregando...' : isLogin ? 'Entrar' : 'Cadastrar'}
        </button>
      </form>

      {isLogin && loginMessages.success && <div className="success">{loginMessages.success}</div>}
      {isLogin && loginMessages.error && <div className="error">{loginMessages.error}</div>}

      {!isLogin && registerMessages.success && <div className="success">{registerMessages.success}</div>}
      {!isLogin && registerMessages.error && <div className="error">{registerMessages.error}</div>}

      <button className={styles.toggleButton} onClick={toggleForm}>
        {isLogin ? 'Não tem uma conta? Cadastre-se' : 'Já tem uma conta? Faça Login'}
      </button>

      <button className={styles.toggleButton} onClick={() => navigate('/forgot-password')}>
        Esqueceu a senha?
      </button>

      {isVerificationModalVisible && (
        <EmailVerificationModal
          onClose={() => setIsVerificationModalVisible(false)}
          onResend={() => sendVerificationEmail(email)}
          isEmailSent={isEmailSent}
        />
      )}
    </div>
  );
};

export default UserLog;
