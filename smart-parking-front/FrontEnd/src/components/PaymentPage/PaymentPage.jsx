import React, { useContext } from 'react';
import { AuthContext } from '../../context/AuthProvider';
import styles from './PaymentPage.module.css';

const PaymentPage = () => {
  const { paymentHistory } = useContext(AuthContext); // Acessando o histórico de pagamentos do contexto

  return (
    <div>
      <h1 className={styles.title}>Histórico de Pagamentos</h1>
      <div className={styles.container}>
        {paymentHistory.length === 0 ? (
          <p>Nenhum pagamento realizado ainda.</p>
        ) : (
          paymentHistory.map((payment, index) => (
            <div key={index} className={styles.container_pay}>
              <div className={styles.fieldcontainer}>
                <div className={styles.field}>
                  <label>Cliente:</label>
                  <p>{payment.client}</p>
                </div>
                <div className={styles.field}>
                  <label>Estacionamento:</label>
                  <p>{payment.parking}</p>
                </div>
                <div className={styles.field}>
                  <label>Modelo:</label>
                  <p>{payment.model}</p>
                </div>
                <div className={styles.field}>
                  <label>Cor:</label>
                  <p>{payment.color}</p>
                </div>
               
                <div className={styles.field}>
                  <label>Placa:</label>
                  <p>{payment.plate}</p>
                </div>
                <div className={styles.field}>
                  <label>Valor:</label>
                  <p>{payment.amount}</p>
                </div>
                <div className={styles.field}>
                  <label>Data:</label>
                  <p>{new Date(payment.date).toLocaleDateString()}</p>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default PaymentPage;
