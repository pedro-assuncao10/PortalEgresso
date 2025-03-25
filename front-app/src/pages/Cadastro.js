import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function Cadastro() {
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState({ login: "", senha: "", tipo: "egresso" });

  const handleChange = (e) => {
    setUsuario({ ...usuario, [e.target.name]: e.target.value });
  };

  const handleCadastro = () => {
    axios.post("http://localhost:8080/api/coordenadores/cadastro", usuario)
      .then((response) => {
        localStorage.setItem("usuario", JSON.stringify(response.data));
        navigate("/CadastroEgresso");
      })
      .catch((error) => {
        console.error("Erro ao cadastrar usuário:", error);
      });
  };

  const handleLogin = () => {
    navigate("/"); // Ajuste a rota conforme necessário
  };

  return (
    <div style={{
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      height: "100vh",
      textAlign: "center",
    }}>
      <div>
        <h1>Portal Egresso</h1>
        <p style={{ fontSize: "16px", color: "#555", marginBottom: "20px" }}>
          Cadastre-se com Usuário e Senha
        </p>

        {/* Campo de Login */}
        <div style={{ backgroundColor: "#4CAF50", borderRadius: "25px", width: "400px", height: "40px", display: "flex", justifyContent: "center", alignItems: "center", margin: "10px auto" }}>
          <input
            type="text"
            name="login"
            placeholder="Login"
            value={usuario.login}
            onChange={handleChange}
            style={{ border: "none", padding: "5px 10px", borderRadius: "10px", flex: 1 }}
          />
        </div>

        {/* Campo de Senha */}
        <div style={{ backgroundColor: "#4CAF50", borderRadius: "25px", width: "400px", height: "40px", display: "flex", justifyContent: "center", alignItems: "center", margin: "10px auto" }}>
          <input
            type="password"
            name="senha"
            placeholder="Senha"
            value={usuario.senha}
            onChange={handleChange}
            style={{ border: "none", padding: "5px 10px", borderRadius: "10px", flex: 1 }}
          />
        </div>

        {/* Campo de Seleção do Tipo de Usuário */}
        <div style={{ backgroundColor: "#4CAF50", borderRadius: "25px", width: "400px", height: "40px", display: "flex", justifyContent: "center", alignItems: "center", margin: "10px auto" }}>
          <select 
            name="tipo" 
            value={usuario.tipo} 
            onChange={handleChange} 
            style={{ border: "none", padding: "5px 10px", borderRadius: "10px", flex: 1, background: "transparent", color: "#fff" }}
          >
            <option value="egresso" style={{ color: "#000" }}>Egresso</option>
            <option value="coordenador" style={{ color: "#000" }}>Coordenador</option>
          </select>
        </div>

        {/* Botão de Cadastro */}
        <button onClick={handleCadastro} style={{ backgroundColor: "#4CAF50", color: "#fff", padding: "10px 20px", border: "none", borderRadius: "25px", width: "400px", fontSize: "16px", cursor: "pointer", marginTop: "20px" }}>
          Cadastrar
        </button>

        {/* Frase de Login */}
        <p style={{ fontSize: "14px", color: "#555", marginTop: "15px" }}>
          Pertence à comunidade? <b>Entre!</b>
        </p>

        {/* Botão de Login */}
        <button onClick={handleLogin} style={{ backgroundColor: "#4CAF50", color: "#fff", padding: "10px 20px", border: "none", borderRadius: "25px", width: "400px", fontSize: "16px", cursor: "pointer", marginTop: "10px" }}>
          Entrar
        </button>

        <p style={{ fontSize: "12px", color: "#4CAF50", opacity: 0.5, marginTop: "20px" }}>
          PAdevelopment
        </p>
      </div>
    </div>
  );
}
