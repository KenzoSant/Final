
#!/usr/bin/bash
cd "smart-parking"
pwd
echo "========================================================================"
echo "INICIANDO APLICAÇÃO JAVA!"
echo "========================================================================"
./init-system.sh 

cd ..
sudo apt update
sudo apt install -y nodejs
cd "smart-parking-front/FrontEnd"
pwd
echo "========================================================================"
echo "INICIANDO FRONTEND!"
echo "========================================================================"
npm install
npm run dev
