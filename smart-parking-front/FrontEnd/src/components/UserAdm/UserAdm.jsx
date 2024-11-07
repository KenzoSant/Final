import React, { useContext, useState, useEffect } from 'react';
import { AuthContext } from '../../context/AuthProvider';
import './UserAdm.css';

const UserAdm = () => {
  const { logout } = useContext(AuthContext);
  const [records, setRecords] = useState([]);
  const [clients, setClients] = useState([]);
  const [averageMonths, setAverageMonths] = useState([]);
  const [activeSection, setActiveSection] = useState(null); // Controle de seções ativas

  // Função para buscar registros de estacionamento
  const fetchRecords = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/admin/current-records', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Erro ao buscar registros');
      }

      const data = await response.json();
      setRecords(data);
    } catch (error) {
      console.error('Erro ao carregar registros:', error);
    }
  };

  // Função para buscar os clientes cadastrados
  const fetchClients = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/admin/clients', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Erro ao buscar clientes');
      }

      const data = await response.json();
      setClients(data);
    } catch (error) {
      console.error('Erro ao buscar clientes:', error);
    }
  };

  // Função para buscar a média de registros por mês
  const fetchAverageMonths = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/admin/average-months', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });

      if (!response.ok) {
        throw new Error('Erro ao buscar médias mensais');
      }

      const data = await response.json();
      setAverageMonths(data);
    } catch (error) {
      console.error('Erro ao buscar médias mensais:', error);
    }
  };

  const handleSectionToggle = (section) => {
    if (activeSection === section) {
      setActiveSection(null); // Se a seção já estiver aberta, fecha
    } else {
      setActiveSection(section); // Caso contrário, abre a nova seção

      // Chama a função de requisição adequada conforme a seção ativa
      if (section === 'records') {
        fetchRecords();  // Requisita os registros sempre que for abrir a seção
      } else if (section === 'clients') {
        fetchClients();  // Requisita os clientes sempre que for abrir a seção
      } else if (section === 'average') {
        fetchAverageMonths();  // Requisita as médias mensais sempre que for abrir a seção
      }
    }
  };

  return (
    <div className='user-adm'>
      <div className="container-adm">
        {/* Botões para exibir registros, clientes e médias mensais */}
        <div className="toggle-buttons">
          <button onClick={() => handleSectionToggle('records')} className="toggle-button">
            Carros Ativos
          </button>
          <button onClick={() => handleSectionToggle('clients')} className="toggle-button">
            Usuários Cadastrados
          </button>
          <button onClick={() => handleSectionToggle('average')} className="toggle-button">
            Médias Mensais
          </button>
        </div>

        {/* Exibe os registros de estacionamento */}
        {activeSection === 'records' && (
          <div className="records-container">
            <h3>Registros de Estacionamento</h3>
            <div className="records-list">
              {records.map((record, index) => (
                <div className="record-card" key={index}>
                  <p><strong>Cliente:</strong> {record.client_name}</p>
                  <p><strong>Email:</strong> {record.email}</p>
                  <p><strong>Modelo:</strong> {record.model}</p>
                  <p><strong>Placa:</strong> {record.plate}</p>
                  <p><strong>Estacionamento:</strong> {record.parking_name}</p>
                  <p><strong>Entrada:</strong> {new Date(record.entry_time).toLocaleString()}</p>
                  {/* <p><strong>Saída:</strong> {new Date(record.exit_time).toLocaleString()}</p> */}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Exibe os clientes cadastrados */}
        {activeSection === 'clients' && (
          <div className="clients-container">
            <h3>Usuários Cadastrados</h3>
            <div className="clients-list">
              {clients.map((client, index) => (
                <div className="client-card" key={index}>
                  <p><strong>Nome:</strong> {client.name}</p>
                  <p><strong>Email:</strong> {client.email}</p>
                  <p><strong>Papel:</strong> {client.role}</p>
                  <p><strong>Status:</strong> {client.enabled ? 'Ativo' : 'Desativado'}</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Exibe as médias mensais */}
        {activeSection === 'average' && (
          <div className="average-container">
            <h3>Médias Mensais de Registros</h3>
            <div className="average-list">
              {averageMonths.map((item, index) => (
                <div className="average-card" key={index}>
                  <p><strong>Ano:</strong> {item.year}</p>
                  <p><strong>Mês:</strong> {item.month}</p>
                  <p><strong>Média de Registros:</strong> {item.averageRecords}</p>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      <button onClick={logout} className="logout-button">
        Logout
      </button>
    </div>
  );
};

export default UserAdm;
