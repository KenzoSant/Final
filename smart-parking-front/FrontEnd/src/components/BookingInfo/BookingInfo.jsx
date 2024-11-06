import React, { useContext, useState, useEffect } from 'react';
import axios from 'axios';
import { AuthContext } from '../../context/AuthProvider';
import { FaRegCopy } from 'react-icons/fa';
import './BookingInfo.css';

const BookingInfo = () => {
  const { user, makePayment } = useContext(AuthContext);
  const [parkingInfo, setParkingInfo] = useState([]);
  const [error, setError] = useState(null);
  const [copySuccess, setCopySuccess] = useState('');

  useEffect(() => {
    if (user) {
      const eventSource = new EventSource(`http://localhost:8080/api/v1/parking-records/${user.id}`);

      eventSource.onmessage = (event) => {
        const data = JSON.parse(event.data);
        console.log('Dados recebidos do SSE:', data);
        if (Array.isArray(data) && data.length > 0) {
          setParkingInfo((prevState) =>
            data.map((car) => {
              const existingCar = prevState.find((prevCar) => prevCar.plate === car.plate);
              return existingCar ? { ...car, ...existingCar } : { ...car, qrCodeUrl: null, qrCodePayload: null, isLoadingQrCode: false, isPaymentConfirmed: false };
            })
          );
        } else {
          setParkingInfo([]);
        }
      };

      eventSource.onerror = (err) => {
        console.error('Erro ao conectar com SSE:', err);
        setError('Erro ao conectar com o servidor.');
        eventSource.close();
      };

      return () => {
        eventSource.close();
      };
    }
  }, [user]);

  if (error) {
    return <div>{error}</div>;
  }

  if (parkingInfo.length === 0) {
    return (
      <div className="booking-info">
        <div>Sem estacionamentos...</div>
      </div>
    );
  }

  const handleGenerateQrCode = async (plate, index) => {
    // Se o QR Code já estiver visível, vamos removê-lo
    if (parkingInfo[index].qrCodeUrl) {
      setParkingInfo((prevState) =>
        prevState.map((item, i) => (i === index ? { ...item, qrCodeUrl: null, qrCodePayload: null } : item))
      );
      return;
    }

    // Caso contrário, geramos um novo QR Code
    setParkingInfo((prevState) =>
      prevState.map((item, i) => (i === index ? { ...item, isLoadingQrCode: true } : item))
    );

    try {
      const response = await axios.post('http://localhost:5000/gerar_qrcode', {
        valor: '00.01', // Exemplo de valor
        id_usuario: user.id,
      });

      const { url_qrcode, payload } = response.data;

      setParkingInfo((prevState) =>
        prevState.map((item, i) => (i === index ? { ...item, qrCodeUrl: url_qrcode, qrCodePayload: payload, isLoadingQrCode: false } : item))
      );
    } catch (err) {
      console.error('Erro ao gerar QR Code:', err);
      setParkingInfo((prevState) =>
        prevState.map((item, i) => (i === index ? { ...item, isLoadingQrCode: false } : item))
      );
    }
  };

  const handleConfirmPayment = async (plate, index) => {
    try {
      await makePayment(plate);
      setParkingInfo((prevState) =>
        prevState.map((item, i) => (i === index ? { ...item, isPaymentConfirmed: true, qrCodeUrl: null } : item))
      );
    } catch (err) {
      console.error('Erro ao confirmar pagamento:', err);
      alert('Erro ao confirmar pagamento.');
    }
  };

  const handleCopyPayload = (payload) => {
    navigator.clipboard.writeText(payload).then(() => {
      setCopySuccess('Copiado!');
      setTimeout(() => setCopySuccess(''), 2000);
    }).catch((err) => {
      console.error('Erro ao copiar:', err);
    });
  };

  return (
    <div>
      {parkingInfo.map((carInfo, index) => {
        const { parking, entry_time, plate, current_amount, qrCodeUrl, qrCodePayload, isLoadingQrCode, isPaymentConfirmed } = carInfo;

        const formattedEntryTime = entry_time
          ? new Date(entry_time).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
          : 'Horário inválido';

        return (
          <div key={index} className="booking-info">
            <div className="booking-local">
              <p><strong>{parking || 'Local não disponível'}</strong></p>
            </div>
            
            <div className="booking-content">
              <div className="booking-time">
                <span>Entrada:</span>
                <p><strong>{formattedEntryTime}</strong></p>
              </div>

              <div className="booking-time">
                <span>Placa:</span>
                <p><strong>{plate || 'Placa não disponível'}</strong></p>
              </div>

              <div className="booking-time">
                <span>Valor:</span>
                <p><strong>R${current_amount || 'Valor não disponível'}</strong></p>
              </div>
            </div>

            <button className="find-location-btn" onClick={() => handleGenerateQrCode(plate, index)} disabled={isLoadingQrCode || isPaymentConfirmed}>
              {isLoadingQrCode ? 'Gerando QR Code...' : qrCodeUrl ? 'Ocultar QR Code' : 'Pagamento'}
            </button>

            {qrCodeUrl && (
              <div className="qr-code-container">
                <img src={qrCodeUrl} alt="QR Code para pagamento" />
                <div className="booking-time cod">
                  <span><strong>Código:</strong> {qrCodePayload?.slice(0, 10)}...</span>
                  <button className="copy-payload-btn" onClick={() => handleCopyPayload(qrCodePayload)}>
                    <FaRegCopy />
                  </button>
                  {copySuccess && <span className="copy-success">{copySuccess}</span>}
                </div>

                <button className="find-location-btn" onClick={() => handleConfirmPayment(plate, index)} disabled={isPaymentConfirmed}>
                  {isPaymentConfirmed ? 'Pagamento Confirmado' : 'Confirmar Pagamento'}
                </button>
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
};

export default BookingInfo;
