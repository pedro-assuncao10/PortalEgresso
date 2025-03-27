# Usa uma versão mais recente do Node.js
FROM node:20

WORKDIR /app

# Copia apenas os arquivos necessários primeiro
COPY package.json package-lock.json ./

# Instala as dependências
RUN npm install

# Copia o código-fonte
COPY . .

EXPOSE 3000

CMD ["npm", "start"]
