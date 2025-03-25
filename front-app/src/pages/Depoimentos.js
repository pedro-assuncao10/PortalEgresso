import React, { useEffect, useState } from "react"; 
import { Container, Paper, Typography, Box, IconButton, Drawer, Button, TextField, Card } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";  // Importa o ícone de voltar
import CloseIcon from "@mui/icons-material/Close";
import axios from "axios";
import { data, useNavigate } from "react-router-dom";

export default function Depoimentos() {
  const [depoimentos, setDepoimentos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [menuAberto, setMenuAberto] = useState(false);
  const [novoDepoimento, setNovoDepoimento] = useState("");
  const [painelAberto, setPainelAberto] = useState(false);
  const [modoEdicao, setModoEdicao] = useState(false);
  const [idDepoimentoEdicao, setIdDepoimentoEdicao] = useState(null);
  const [depoimentosEgresso, setDepoimentosEgresso] = useState([]);
  const [idCoordenador, setIdCoordenador] = useState(null);
  const [painelSelecaoAberto, setPainelSelecaoAberto] = useState(false);
  const navigate = useNavigate();
  // Função para verificar o tipo de usuário no localStorage
  const verificarTipoUsuario = () => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    return usuario ? usuario.tipo : null; // Retorna o tipo de usuário ou null se não houver usuário
  };

  useEffect(() => {
    const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));
    if (!usuarioLogado) {
      navigate("/login");
      return;
    }

    setIdCoordenador(usuarioLogado.idCoordenador);

    async function carregarDepoimentos() {
      try {
        const resposta = await axios.get("http://localhost:8080/api/depoimentos");
        setDepoimentos(resposta.data);
      } catch (error) {
        console.error("Erro ao buscar depoimentos", error);
      } finally {
        setLoading(false);
      }
    }
    carregarDepoimentos();
  }, []);

  useEffect(() => {
    async function pegarIdDepoimentoEgresso() {
      if (idCoordenador) {
        try {
          const resposta = await axios.get(`http://localhost:8080/api/depoimentos/porEgresso/${idCoordenador}`);
          setDepoimentosEgresso(resposta.data);
        } catch (error) {
          console.error("Erro ao buscar depoimentos do egresso", error);
        }
      }
    }
    pegarIdDepoimentoEgresso();
  }, [idCoordenador]);

  const abrirPainelEdicao = (id, texto) => {
    console.log("Abrindo edição - ID:", id, "Texto:", texto); // Log para depuração
    setModoEdicao(true);
    setPainelAberto(true);
    setIdDepoimentoEdicao(id);
    setNovoDepoimento(texto);
  };
  
  const abrirPainelSelecao = () => {
    setModoEdicao(true);
    setPainelSelecaoAberto(true);
  };  

  const fecharPainelSelecao = () => {
    setPainelSelecaoAberto(false);
  }; 
  const criarDepoimento = async () => {
    if (!novoDepoimento.trim()) {
      alert("Digite um depoimento!");
      return;
    }
    try {
      await axios.post("http://localhost:8080/api/depoimentos", {
        idEgresso: idCoordenador,
        texto: novoDepoimento,
      });
      setNovoDepoimento("");
      setPainelAberto(false);
      window.location.reload();
    } catch (error) {
      console.error("Erro ao criar depoimento", error);
    }
  };

  const editarDepoimento = async () => {
    console.log("Novo Depoimento:", novoDepoimento, idDepoimentoEdicao,data);
    try {
      await axios.put("http://localhost:8080/api/depoimentos", {
        idDepoimento: idDepoimentoEdicao,
        texto: novoDepoimento,
        idEgresso: setIdCoordenador,
        data: new Date().toISOString().split("T")[0],
      });

      setPainelAberto(false);
      setModoEdicao(false);
      window.location.reload(); // Recarrega a página para buscar os dados atualizados

    } catch (error) {
      console.error("Erro ao editar depoimento", error);
    }
  };


  const deletarDepoimento = async (idDepoimento) => {
    try {
      await axios.delete("http://localhost:8080/api/depoimentos", {
        data: { idDepoimento: idDepoimento },
      });
      window.location.reload();
    } catch (error) {
      console.error("Erro ao deletar depoimento", error);
    }
  };

  const iniciarChat = (nome) => {
    alert(`Iniciando chat com ${nome}`);
  };

  if (loading) {
    return <Typography align="center">Carregando...</Typography>;
  }

  return (
    <Container maxWidth="md" sx={{ mt: 5 }}>
      <Box display="flex" justifyContent="center" alignItems="center" position="relative">
        {/* Ícone de voltar */}
        <IconButton
          sx={{ position: "absolute", left: 0 }}
          onClick={() => {
            const tipoUsuario = verificarTipoUsuario();

            if (tipoUsuario === "coordenador") {
              navigate("/homeCoordenador"); // Navega para a Home do Coordenador
            } else {
              navigate("/home"); // Navega para a Home do Egresso
            }
          }}
        >
          <ArrowBackIcon />
        </IconButton>
        <Typography variant="h4" align="center" gutterBottom>
          Depoimentos dos Egressos
        </Typography>
        <IconButton sx={{ position: "absolute", right: 0 }} onClick={() => setMenuAberto(true)}>
          <MenuIcon />
        </IconButton>
      </Box>

      {depoimentos.length === 0 ? (
        <Typography align="center">Nenhum depoimento encontrado!</Typography>
      ) : (
        depoimentos.map((dep) => (
          <Box key={dep.idDepoimento} sx={{ mb: 4, display: "flex", flexDirection: "column", alignItems: "center" }}>
            <Paper
              sx={{
                backgroundColor: "#4CAF50",
                color: "white",
                borderRadius: "20px",
                padding: "10px 20px",
                width: "80%",
                textAlign: "center",
                fontWeight: "bold",
              }}
            >
              {dep.egresso.nome} • {dep.egresso.descricao}
            </Paper>
            <Paper
              sx={{
                backgroundColor: "#A5D6A7",
                color: "black",
                borderRadius: "20px",
                padding: "20px",
                marginTop: "10px",
                width: "80%",
                minHeight: "120px",
                textAlign: "justify",
                fontSize: "16px",
              }}
            >
              {dep.texto}
            </Paper>
          </Box>
        ))
      )}

      <Drawer anchor="right" open={menuAberto} onClose={() => setMenuAberto(false)}>
        <Box sx={{ width: 250, p: 2, display: "flex", flexDirection: "column", gap: 2 }}>
          <IconButton sx={{ alignSelf: "flex-end" }} onClick={() => setMenuAberto(false)}>
            <CloseIcon />
          </IconButton>
          <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={() => setPainelAberto(true)}>
            Criar Novo Depoimento
          </Button>
          <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={abrirPainelSelecao}>
            Editar Depoimento
          </Button>
          <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={abrirPainelSelecao}>
            Deletar Depoimento
          </Button>
        </Box>
      </Drawer>
      {painelSelecaoAberto && depoimentosEgresso.length > 0 && (
        <Card
          sx={{
            p: 3,
            bgcolor: "#A5D6A7",
            borderRadius: 3,
            width: "100%",
            maxWidth: 400,
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            zIndex: 1000,
            overflowY: "auto",
            maxHeight: "400px",
          }}
        >
          <Typography color="black" fontWeight="bold" mb={2}>
            Selecione o Depoimento para Criar,Editar ou Deletar
          </Typography>
          {depoimentosEgresso.map((dep) => (
            <Paper
              key={dep.idDepoimento}
              sx={{
                p: 2,
                mb: 2,
                bgcolor: "#4CAF50",
                color: "white",
                cursor: "pointer",
                textAlign: "center",
              }}
              onClick={() => {
                if (modoEdicao) {
                  abrirPainelEdicao(dep.idDepoimento, dep.texto);
                  setPainelSelecaoAberto(false);
                }
              }}
              onDoubleClick={() => {
                if (!modoEdicao) {
                  if (window.confirm("Tem certeza que deseja deletar este depoimento?")) {
                    deletarDepoimento(dep.idDepoimento);
                    setPainelSelecaoAberto(false);
                  }
                }
              }}                 
            >
              {dep.texto}
            </Paper>
          ))}
          <Button
            variant="outlined"
            sx={{ color: "black", borderColor: "black", mt: 2 }}
            onClick={fecharPainelSelecao}
          >
            Fechar
          </Button>
        </Card>
      )}

      {/* Painel para Novo Depoimento */}
      {painelAberto && (
        <Card sx={{ p: 3, bgcolor: "#4CAF50", borderRadius: 3, width: "100%", maxWidth: 400, position: "absolute", top: "50%", left: "50%", transform: "translate(-50%, -50%)", zIndex: 1000 }}>
          <Typography color="white" fontWeight="bold" mb={2}>{modoEdicao ? "Editar Depoimento" : "Novo Depoimento"}</Typography>
          <TextField
            fullWidth
            label="Depoimento"
            multiline
            rows={4}
            value={novoDepoimento}
            onChange={(e) => setNovoDepoimento(e.target.value)}
            sx={{ mb: 2, bgcolor: "white" }}
          />
          <Box display="flex" justifyContent="space-between" mt={3}>
            <Button
              variant="contained"
              sx={{ bgcolor: "#4CAF50", mt: 2 }}
              onClick={() => {
                if (modoEdicao) {
                  editarDepoimento();
                } else {
                  criarDepoimento();
                }
              }}
            >
              {modoEdicao ? "Salvar" : "Cadastrar"}
            </Button>
            <Button variant="outlined" sx={{ color: "white", borderColor: "white" }} onClick={() => setPainelAberto(false)}>
              Cancelar
            </Button>
          </Box>
        </Card>
      )}
    </Container>
  );

}
