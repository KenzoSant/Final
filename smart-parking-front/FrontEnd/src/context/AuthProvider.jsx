import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';
import { useNavigate } from 'react-router-dom';

export const AuthContext = createContext();

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const token = localStorage.getItem('token');
    try {
      return token ? jwtDecode(token) : null;
    } catch (error) {
      console.error("Erro ao decodificar o token:", error);
      return null;
    }
  });

  const [loading, setLoading] = useState(false);
  const [vehicles, setVehicles] = useState([]);
  const [colors, setColors] = useState([]);
  const [makes, setMakes] = useState([]);
  const [paymentHistory, setPaymentHistory] = useState([]);
  const [loginMessages, setLoginMessages] = useState({ success: null, error: null });
  const [registerMessages, setRegisterMessages] = useState({ success: null, error: null });
  const [resetPasswordMessages, setResetPasswordMessages] = useState({ success: null, error: null });
  const [changePasswordMessages, setChangePasswordMessages] = useState({ success: null, error: null });
  const [vehicleMessages, setVehicleMessages] = useState({ success: null, error: null });

  const navigate = useNavigate();

  const clearMessages = (type) => {
    if (type === 'login') setLoginMessages({ success: null, error: null });
    if (type === 'register') setRegisterMessages({ success: null, error: null });
    if (type === 'resetPassword') setResetPasswordMessages({ success: null, error: null });
    if (type === 'changePassword') setChangePasswordMessages({ success: null, error: null });
    if (type === 'vehicle') setVehicleMessages({ success: null, error: null });
  };

  const login = async (email, password) => {
    setLoading(true);
    clearMessages('login');
    try {
      const response = await axios.post('http://localhost:8080/api/v1/auth/login', { email, password });
      const token = response.data.token;
  
      if (token) {
        localStorage.setItem('token', token);
        const decodedToken = jwtDecode(token);
  
        if (decodedToken.exp * 1000 < Date.now()) {
          console.error('Token expirado.');
          logout();
          return;
        }
  
        setUser(decodedToken);
        if (decodedToken.role === 'ADMIN') {
          navigate('/admin');
        } else {
          navigate('/home');
        }
        setLoginMessages({ success: 'Login efetuado com sucesso!' });
      } else {
        setLoginMessages({ error: 'Erro: Token não foi retornado.' });
      }
    } catch (err) {
      console.log("ERRO", err);
  
      // Verifique se a resposta contém a mensagem "Conta desativada"
      if (err.response && err.response.data && err.response.data.message) {
        const errorMessage = err.response.data.message;
  
        // Se a mensagem for "Conta desativada", exiba essa mensagem
        if (errorMessage.includes('Conta desativada')) {
          setLoginMessages({ error: errorMessage });
        } else {
          setLoginMessages({ error: 'Email ou senha incorretos.' });
        }
      } else {
        setLoginMessages({ error: 'Email ou senha incorretos.' });
      }
    } finally {
      setLoading(false);
    }
  };
  

  const logout = () => {
    setUser(null);
    localStorage.removeItem('token');
    navigate('/');
  };

  const register = async (name, email, password) => {
    setLoading(true);
    clearMessages('register');
    try {
      const response = await axios.post('http://localhost:8080/api/v1/auth/register', { name, email, password });
      const token = response.data.token;
      localStorage.setItem('token', token);
      setRegisterMessages({ success: 'Cadastro efetuado com sucesso!' });
    } catch (err) {
      setRegisterMessages({ error: 'Erro ao cadastrar.' });
    } finally {
      setLoading(false);
    }
  };

  const changePassword = async (currentPassword, newPassword) => {
    setLoading(true);
    clearMessages('changePassword');
    try {
      const token = localStorage.getItem('token');
      await axios.put('http://localhost:8080/api/v1/auth/change-password', { currentPassword, newPassword }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setChangePasswordMessages({ success: 'Senha alterada com sucesso!' });
    } catch (error) {
      setChangePasswordMessages({ error: 'Erro ao alterar senha.' });
    } finally {
      setLoading(false);
    }
  };

  const resetPassword = async (currentPassword, newPassword) => {
    setLoading(true);
    clearMessages('resetPassword');
    try {
      const token = localStorage.getItem('token');
      await axios.put('http://localhost:8080/api/v1/auth/reset-password', {
        currentPassword,
        newPassword
      }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setResetPasswordMessages({ success: 'Senha alterada com sucesso!' });
    } catch (error) {
      setResetPasswordMessages({ error: 'Erro ao alterar senha.' });
    } finally {
      setLoading(false);
    }
  };

  const fetchColors = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get('http://localhost:8080/api/v1/colors', {
        headers: { Authorization: `Bearer ${token}` },
      });
      setColors(response.data);
    } catch (error) {
      console.error('Erro ao buscar cores:', error);
    }
  };

  const fetchMakes = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get('http://localhost:8080/api/v1/makecars', {
        headers: { Authorization: `Bearer ${token}` },
      });
      setMakes(response.data);
    } catch (error) {
      console.error('Erro ao buscar marcas:', error);
    }
  };

  const fetchVehicles = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get('http://localhost:8080/api/v1/vehicles', {
        headers: { Authorization: `Bearer ${token}` },
      });
      setVehicles(response.data);
    } catch (error) {
      console.error('Erro ao buscar veículos:', error);
    }
  };

  const createVehicle = async (vehicleData) => {
    clearMessages('vehicle');
    const token = localStorage.getItem('token');
    try {
      await axios.post('http://localhost:8080/api/v1/vehicles', vehicleData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      await fetchVehicles();
      setVehicleMessages({ success: 'Veículo cadastrado com sucesso!' });
    } catch (error) {
      setVehicleMessages({ error: 'Erro ao cadastrar veículo.' });
    }
  };

  const fetchParkingHistory = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get('http://localhost:8080/api/v1/parking-records/history', {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      console.error('Erro ao buscar histórico de estacionamento:', error);
      throw error;
    }
  };

  const makePayment = async (plate) => {
    const token = localStorage.getItem('token');
    const paymentPayload = { plate };

    try {
      const response = await axios.post('http://localhost:8080/api/v1/payments', paymentPayload, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const paymentDataResponse = response.data; 
      setPaymentHistory((prevHistory) => [...prevHistory, paymentDataResponse]);
    } catch (error) {
      console.error('Erro ao processar pagamento:', error);
    }
  };

  return (
    <AuthContext.Provider value={{
      user,
      loading,
      login,
      register,
      resetPassword,
      changePassword,
      createVehicle,
      clearMessages,
      loginMessages,
      registerMessages,
      resetPasswordMessages,
      changePasswordMessages,
      vehicleMessages,
      fetchVehicles,
      fetchColors,
      fetchMakes,
      fetchParkingHistory,
      logout,
      vehicles,
      colors,
      makes,
      makePayment,
      paymentHistory
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
