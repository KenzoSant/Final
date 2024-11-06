import React, { useContext, useState, useEffect } from 'react';
import { AuthContext } from '../../context/AuthProvider';
import './UserPage.css';
import { FaEye, FaEyeSlash } from 'react-icons/fa'; 

const UserPage = () => {
  const { user, logout, createVehicle, vehicles, fetchVehicles, resetPassword, loading, clearMessages, vehicleMessages, resetPasswordMessages } = useContext(AuthContext);
  const [vehicle, setVehicle] = useState({ make: '', model: '', plate: '', year: '', color: '' });
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const { fetchColors, fetchMakes, colors, makes } = useContext(AuthContext);
  const [showCurrentPassword, setShowCurrentPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    fetchVehicles();
    fetchColors();
    fetchMakes();
    clearMessages('resetPasswords');
    clearMessages('vehiclems');
  }, []);

  const handleAddVehicle = async () => {
    if (!validatePlate(vehicle.plate)) {
      setErrorMessage('Placa inválida');
      return;
    }

    if (!validateYear(vehicle.year)) {
      setErrorMessage('Ano inválido');
      return;
    }

    setErrorMessage('');

    try {
      await createVehicle(vehicle);
      setVehicle({ make: '', model: '', plate: '', year: '', color: '' });
    } catch (error) {
      console.error('Erro ao adicionar veículo:', error);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    resetPassword(currentPassword, newPassword);
  };

  const validatePlate = (plate) => {
    const oldPlateFormat = /^[A-Z]{3}\d{4}$/; // AAA1234
    const newPlateFormat = /^[A-Z]{3}\d{1}[A-Z]{1}\d{2}$/; // AAA1A23
    return oldPlateFormat.test(plate) || newPlateFormat.test(plate);
  };

  const validateYear = (year) => {
    const currentYear = new Date().getFullYear();
    return year >= 1900 && year <= currentYear;
  };

  return (
    <div className="user-page">
      {/* Informações do usuário */}
      <div className="user-info">
        <h1>Usuário</h1>
        <div className="user-container-info">
          <h2>Informações do Usuário</h2>
          <p><strong>Nome:</strong> {user?.name || 'N/A'}</p>
          <p><strong>Email:</strong> {user?.sub || 'N/A'}</p>
        </div>
      </div>

      {/* Alterar senha */}
      <div className="user-info">
        <h1>Senha</h1>
        <div className="user-container-info">
          <h2>Alterar Senha</h2>
          <form onSubmit={handleSubmit}>
            <div className="password-container">
              <input
                type={showCurrentPassword ? 'text' : 'password'}
                placeholder="Senha atual"
                value={currentPassword}
                required
                maxLength={100}
                onChange={(e) => setCurrentPassword(e.target.value)}
              />
              <button 
                type="button" 
                className="showPasswordButton"
                onClick={() => setShowCurrentPassword(!showCurrentPassword)}
              >
                {showCurrentPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>
            <div className="password-container">
              <input
                type={showNewPassword ? 'text' : 'password'}
                placeholder="Nova senha"
                value={newPassword}
                required
                maxLength={100}
                onChange={(e) => setNewPassword(e.target.value)}
              />
              <button 
                type="button" 
                className="showPasswordButton"
                onClick={() => setShowNewPassword(!showNewPassword)}
              >
                {showNewPassword ? <FaEyeSlash /> : <FaEye />}
              </button>
            </div>
            <button 
              className='alterar' 
              type="submit" 
              disabled={loading}
            >
              Alterar Senha
            </button>

            {resetPasswordMessages.success && <div className="success">{resetPasswordMessages.success}</div>}
            {resetPasswordMessages.error && <div className="error">{resetPasswordMessages.error}</div>}
          </form>
        </div>
      </div>

      {/* Exibir veículos */}
      <div className="user-info">
        <h1>Carros</h1>
        <div className="user-container-info">
          <h2>Informações dos Carros</h2>
          {vehicles.length > 0 ? (
            vehicles.map((v, index) => (
              <div key={index} className="vehicle-block">
                <p><strong>Marca:</strong> {v.make}</p>
                <p><strong>Modelo:</strong> {v.model}</p>
                <p><strong>Placa:</strong> {v.plate}</p>
                <p><strong>Ano:</strong> {v.year}</p>
                <p><strong>Cor:</strong> {v.color}</p>
              </div>
            ))
          ) : (
            <p>Nenhum veículo cadastrado.</p>
          )}
        </div>
      </div>

      {/* Formulário para adicionar veículo */}
      <div className="add-vehicle">
        <h2>Adicionar Veículo</h2>
        <select
          value={vehicle.make}
          required
          onChange={(e) => setVehicle({ ...vehicle, make: e.target.value })}
        >
          <option value="">Selecione a marca</option>
          {makes.map((make, index) => (
            <option key={index} value={make.name}>{make.name}</option>
          ))}
        </select>
        <input
          type="text"
          value={vehicle.model}
          required
          maxLength={50}
          onChange={(e) => setVehicle({ ...vehicle, model: e.target.value })}
          placeholder="Modelo"
        />
        <input
          type="text"
          required
          maxLength={7}
          value={vehicle.plate}
          onChange={(e) => {
            const value = e.target.value.toUpperCase();
            setVehicle({ ...vehicle, plate: value });
          }}
          placeholder="Placa"
        />
        <input
          type="number" 
          value={vehicle.year}
          required
          onChange={(e) => {
            const value = e.target.value;
            if (value.length <= 4) {
              setVehicle({ ...vehicle, year: value });
            }
          }}
          placeholder="Ano"
        />
        <select
          value={vehicle.color}
          required
          onChange={(e) => setVehicle({ ...vehicle, color: e.target.value })}
        >
          <option value="">Selecione a cor</option>
          {colors.map((color, index) => (
            <option key={index} value={color.name}>{color.name}</option>
          ))}
        </select>
        <button onClick={handleAddVehicle}>Adicionar</button>

        {errorMessage && <div className="error">{errorMessage}</div>}
        {vehicleMessages.success && <div className="success">{vehicleMessages.success}</div>}
        {vehicleMessages.error && <div className="error">{vehicleMessages.error}</div>}
      </div>

      <button onClick={logout} className="logout-button">
        Logout
      </button>
    </div>
  );
};

export default UserPage;
