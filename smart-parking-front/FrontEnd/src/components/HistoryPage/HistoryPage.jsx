import React, { useEffect, useState, useContext } from 'react';
import '../LocationDisplay/LocationDisplay.css';
import { AuthContext } from '../../context/AuthProvider';
import { assets } from '../../assets/js/assets';

const HistoryPage = ({ isHomePage = false }) => { // Adiciona a prop `isHomePage`
  const { fetchParkingHistory } = useContext(AuthContext); 
  const [locations, setLocations] = useState([]);
  const token = localStorage.getItem('token'); 

  useEffect(() => {
    const getParkingHistory = async () => {
      try {
        const parkingHistory = await fetchParkingHistory(token);
        setLocations(parkingHistory);
      } catch (error) {
        console.error('Erro ao buscar o histórico de estacionamentos:', error);
      }
    };

    if (token) {
      getParkingHistory();
    }
  }, [token, fetchParkingHistory]); 

  return (
    <div className="location-display">
      <h1>{isHomePage ? 'Últimos Estacionamentos' : 'Estacionamentos utilizados'}</h1> {/* Título dinâmico */}

      {locations.length > 0 ? (
        locations.map((location, index) => (
          <div key={index} className="location">
            <img src={assets[`img${index % 4 + 1}`]} alt={location.name} />
            <div className="location-info">
              <h3>{location.name}</h3>
              <p>Valor Pago - <strong>R${location.amount}</strong></p>
              <p>Carro - <strong>{location.model}</strong></p>
            </div>
          </div>
        ))
      ) : (
        <p>Nenhum histórico de estacionamentos disponível.</p>
      )}
    </div>
  );
};

export default HistoryPage;
