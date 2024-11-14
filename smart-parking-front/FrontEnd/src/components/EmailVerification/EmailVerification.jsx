import React from 'react';
import styles from './EmailVerification.module.css';

const EmailVerificationModal = ({ onClose, onResend, isEmailSent, isVerified }) => {
  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <button className={styles.closeButton} onClick={onClose}>
          &times;
        </button>
        <h2>Email de verificação enviado!</h2> <br />
        <p>Por favor, verifique seu email para continuar.</p>
        <p>Email não chegou?</p>
        <button className={styles.resendButton} onClick={onResend}>
              Enviar novamente
        </button>

        {/* {isVerified ? (
          <p>Seu email foi verificado! Agora você pode fazer login.</p>
        ) : (
          <>
            {isEmailSent ? (
              <p>Email de verificação enviado com sucesso!</p>
            ) : (
              <p>Email não chegou?</p>
            )}
            <button className={styles.resendButton} onClick={onResend}>
              Enviar novamente
            </button>
          </>
        )} */}
      </div>
    </div>
  );
};

export default EmailVerificationModal;
