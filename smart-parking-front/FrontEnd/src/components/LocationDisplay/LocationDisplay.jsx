import React from 'react';
import './LocationDisplay.css';
import { assets } from '../../assets/js/assets';

const LocationDisplay = () => {
  return (
    <div className="location-display">
      <h1>Estacionamentos mais utilizados</h1>
      <div className="location">
        <img src={assets.img1} alt="North Excelsior District" />
        <div className="location-info">
          <h3>Estabelecimento</h3>
          <p>Endereço</p>
          <p>Carro - <strong>R$50</strong></p>
        </div>
      </div>

      <div className="location">
        <img src={assets.img2} alt="Manila Oriental Mall" />
        <div className="location-info">
          <h3>Estabelecimento</h3>
          <p>Endereço</p>
          <p>Carro - <strong>R$60</strong></p>
        </div>
      </div>
    </div>
  );
};

export default LocationDisplay;
