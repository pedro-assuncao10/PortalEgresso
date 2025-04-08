import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api"; // Importa o Axios configurado

export default function Login() {
  const navigate = useNavigate();
  const [login, setLogin] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      // Fazendo a requisição para o login
      const response = await api.post(
        "/coordenadores/login",
        { login, senha },
        { headers: { "Content-Type": "application/json" } }
      );

      const { token, coordenador } = response.data; // Objeto do coordenador

      // Armazenando o token e os dados do usuário no localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("usuario", JSON.stringify(coordenador));

      // Verificando o tipo do usuário
      if (coordenador.tipo === "egresso") {
        navigate("/home"); // Redireciona para a Home do Egresso
      } else if (coordenador.tipo === "coordenador" || coordenador.tipo === "super") {
        navigate("/homeCoordenador"); // Redireciona para a Home do Coordenador
      } else {
        setErro("Tipo de usuário inválido!");
        console.error("Tipo de usuário inválido:", coordenador.tipo);
      }
    } catch (error) {
      setErro("Usuário ou senha inválidos!");
      console.error("Erro ao fazer login:", error);
    }
  };

  return (
    <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh", textAlign: "center" }}>
      <div>
        <h1>Portal Egresso</h1>
        <p style={{ fontSize: "16px", color: "#555", marginBottom: "20px" }}>
          Entre com seu usuário e senha e tenha acesso ao app
        </p>

        {/* Formulário corrigido */}
        <form onSubmit={handleLogin}>
          {/* Campo de Login */}
          <div style={{ backgroundColor: "#4CAF50", borderRadius: "25px", width: "400px", height: "40px", display: "flex", alignItems: "center", margin: "10px auto", padding: "5px 10px" }}>
            <input type="text" placeholder="Login" value={login} onChange={(e) => setLogin(e.target.value)} style={{ border: "none", flex: 1, borderRadius: "10px" }} required />
          </div>

          {/* Campo de Senha */}
          <div style={{ backgroundColor: "#4CAF50", borderRadius: "25px", width: "400px", height: "40px", display: "flex", alignItems: "center", margin: "10px auto", padding: "5px 10px" }}>
            <input type="password" placeholder="Senha" value={senha} onChange={(e) => setSenha(e.target.value)} style={{ border: "none", flex: 1, borderRadius: "10px" }} required />
          </div>

          {/* Exibir erro de login, se houver */}
          {erro && <p style={{ color: "red" }}>{erro}</p>}

          {/* ✅ Agora o botão de login está com "type=submit" para evitar problemas */}
          <button type="submit" style={{ backgroundColor: "#4CAF50", color: "#fff", padding: "10px 20px", border: "none", borderRadius: "25px", width: "400px", fontSize: "16px", cursor: "pointer", marginTop: "20px" }}>
            Entrar
          </button>
        </form>

        {/* Texto e botão de cadastro
        <p style={{ marginTop: "20px", fontSize: "16px", color: "#555" }}>
          Novo por aqui? Cadastre-se!
        </p>
        <button
          onClick={() => navigate("/cadastro")}
          style={{
            backgroundColor: "#4CAF50",
            color: "#fff",
            padding: "10px 20px",
            border: "none",
            borderRadius: "25px",
            width: "400px",
            fontSize: "16px",
            cursor: "pointer",
          }}
        >
          Cadastro
        </button> */}

        {/* Rodapé */}
        <p style={{ fontSize: "12px", color: "#4CAF50", opacity: 0.5, marginTop: "20px" }}>
          PAdevelopment
        </p>
      </div>
    </div>
  );
}
