import React from 'react';
import Header from '../../components/Header/Header';
import PaymentPage from '../../components/PaymentPage/PaymentPage';

import './Payment.css'

const Payment = () => {
  return (
    <div>
      <Header />
      <div className="payment-container">
        <PaymentPage/>
      </div>
    </div>
  );
};

export default Payment;
