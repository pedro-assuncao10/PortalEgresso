# Etapa 1: Usar a imagem do Node.js
FROM node:16

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o package.json e o package-lock.json
COPY package.json package-lock.json ./

# Instalar as dependências
RUN npm install

# Copiar o código-fonte da aplicação
COPY . .

# Expor a porta onde o React irá rodar (3000 por padrão)
EXPOSE 3000

# Rodar a aplicação React no modo de desenvolvimento
CMD ["npm", "start"]
