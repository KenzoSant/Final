# API REST Smart Parking

Bem-vindo à API REST Smart Parking! Esta API é responsável por gerenciar os recursos relacionados a um estacionamento, como clientes, pagamentos, registros, etc.

## Tecnologias Utilizadas

- **Docker**: Containerização da aplicação e de seus serviços.
- **Docker Compose**: Orquestração de múltiplos containers Docker.
- **PostgreSQL**: Banco de dados relacional utilizado para armazenar dados.
- **Java**: Linguagem de programação utilizada para desenvolver a API.
- **Spring Boot**: Framework para criar aplicações Java baseadas em Spring de forma rápida.
- **Spring Security**: Framework para gerenciar a segurança e autenticação da aplicação.

## Instruções para Execução
Para executar a aplicação, siga os passos abaixo:

### 1. Clone o repositório:
Primeiro, clone o repositório do projeto:
```bash
git clone https://github.com/victormarinho1/smart-parking.git
```

### 2. Navegue para o diretório do projeto
Depois de clonar o repositório, entre no diretório do projeto:
```bash
cd smart-parking
```
### 3. Configure as Variáveis de Ambiente
Preencha as variáveis de ambiente necessárias no arquivo .env-example:
```bash
EMAIL_USERNAME='seu_email@example.com'
EMAIL_PASSWORD='sua_senha_de_app'
```

### 4. Configuração de ambiente
Certifique-se de ter o Docker e o Docker Compose instalados em sua máquina. Em seguida, execute o comando abaixo para iniciar os serviços necessários:
```bash
./init-system.sh
```
Este comando irá iniciar os serviços necessários, como o banco de dados PostGres, e deixá-los rodando em segundo plano.



## Documentação da API
A documentação da API está acessível por meio do Swagger. Para consultá-la, visite o seguinte link:
```bash
http://localhost:8080/docs/documentation
```