import axios from "axios";

// Criando instância do Axios
const api = axios.create({
  baseURL: "https://merry-amazement-production.up.railway.app/", 
  withCredentials: true, // Garante que cookies sejam enviados (se necessário)
  headers: {
    "Content-Type": "application/json",
  },
});

// Intercepta requisições para adicionar o token JWT automaticamente
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Lida com erros de autenticação (401) e autorização (403)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      const status = error.response.status;
      
      if (status === 401) {
        console.warn("🔒 Sessão expirada! Redirecionando para login...");
      } else if (status === 403) {
        console.warn("⛔ Acesso negado! Você não tem permissão para esta ação.");
      }

      if (status === 401 || status === 403) {
        localStorage.removeItem("token");
        localStorage.removeItem("usuario");
        window.location.href = "/login";
      }
    } else {
      console.error("⚠️ Erro na conexão com o servidor!");
    }
    
    return Promise.reject(error);
  }
);

export default api;
