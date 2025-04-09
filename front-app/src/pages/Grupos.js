import React, { useEffect, useState } from "react"; 
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Box, Typography, Paper, AppBar, Toolbar, IconButton, Drawer, Button, TextField } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import CloseIcon from "@mui/icons-material/Close";

export default function Grupos() {
  const navigate = useNavigate();
  const [grupos, setGrupos] = useState([]);
  const [menuAberto, setMenuAberto] = useState(false);
  const [criandoGrupo, setCriandoGrupo] = useState(false);
  const [idCoordenador, setIdCoordenador] = useState(null);
  const [novoGrupo, setNovoGrupo] = useState({
    nome: "",
    participantes: [],
  });
  // Função para verificar o tipo de usuário no localStorage
  const verificarTipoUsuario = () => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    return usuario ? usuario.tipo : null; // Retorna o tipo de usuário ou null se não houver usuário
  };
  const BASE_URL = "https://merry-amazement-production.up.railway.app";

  // Pega os grupos do backend
  useEffect(() => {
    axios
      .get(`${BASE_URL}/api/grupos`)
      .then((response) => {
        const grupos = Array.isArray(response.data) ? response.data : [response.data];
        setGrupos(grupos);
      })
      .catch((error) => {
        console.error("Erro ao buscar grupos:", error);
      });
  }, []);

  // Obtém o ID do coordenador (usuário logado)
  useEffect(() => {
    const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));
    if (!usuarioLogado) {
      navigate("/login");
      return;
    }
    setIdCoordenador(usuarioLogado.idCoordenador); // Armazena o ID do coordenador logado
  }, [navigate]);

  const criarGrupo = async () => {
    if (!idCoordenador) {
      console.error("Erro: Usuário não autenticado.");
      return;
    }

    const grupoData = {
      nome: novoGrupo.nome,
      criador: { idEgresso: idCoordenador },
      participantes: novoGrupo.participantes,
    };

    try {
      const response = await axios.post(`${BASE_URL}/api/grupos`, grupoData);
      setGrupos([...grupos, response.data]);
      setCriandoGrupo(false);
      setNovoGrupo({ nome: "", participantes: [] });
    } catch (error) {
      console.error("Erro ao criar grupo:", error);
    }
  };

  const entrarNoChat = (grupoId, participantes, criador) => {
    // Verifica se o usuário logado é um dos participantes ou o criador do grupo
    const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));
    
    // Verifica se o usuário logado é o coordenador do grupo ou um dos participantes
    const isMembro = participantes.some((participante) => participante.idEgresso === usuarioLogado.idCoordenador);
    const isCriador = criador.idEgresso === usuarioLogado.idCoordenador;
  
    if (isMembro || isCriador) {
      navigate(`/chat/${grupoId}`);
    } else {
      alert("Você não é membro deste grupo e não pode acessar o chat.");
    }
  };
  
  return (
    <Box sx={{ display: "flex", flexDirection: "column", gap: 2, p: 3, bgcolor: "#E8F5E9", minHeight: "100vh" }}>
      <AppBar position="static" sx={{ backgroundColor: "#4CAF50" }}>
        <Toolbar>
          <IconButton
            edge="start" color="inherit" onClick={() => { const tipoUsuario = verificarTipoUsuario(); if (tipoUsuario === "coordenador") {navigate("/homeCoordenador");} else {navigate("/home");}}}>
            <ArrowBackIcon />
          </IconButton>
          <Typography variant="h6" sx={{ flexGrow: 1, textAlign: "center" }}>
            Conversas
          </Typography>
          <IconButton edge="end" color="inherit" onClick={() => setMenuAberto(true)}>
            <MenuIcon />
          </IconButton>
        </Toolbar>
      </AppBar>

      {/* Menu Lateral */}
      <Drawer anchor="right" open={menuAberto} onClose={() => setMenuAberto(false)}>
        <Box sx={{ width: 250, p: 2, display: "flex", flexDirection: "column", gap: 2, bgcolor: "#E8F5E9" }}>
          <IconButton sx={{ alignSelf: "flex-end" }} onClick={() => setMenuAberto(false)}>
            <CloseIcon />
          </IconButton>

          <Button variant="contained" sx={{ bgcolor: "#4CAF50", "&:hover": { bgcolor: "#388E3C" } }}>
            Perfil
          </Button>
          <Button variant="contained" sx={{ bgcolor: "#4CAF50", "&:hover": { bgcolor: "#388E3C" } }}>
            Configurações
          </Button>
          <Button
            variant="contained"
            sx={{ bgcolor: "#4CAF50", "&:hover": { bgcolor: "#388E3C" } }}
            onClick={() => setCriandoGrupo(true)}
          >
            Criar novo grupo
          </Button>
        </Box>
      </Drawer>

      {/* Formulário para Criar Grupo */}
      {criandoGrupo && (
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2, p: 3, bgcolor: "#C8E6C9", borderRadius: 3 }}>
          <TextField
            label="Nome do Grupo"
            variant="outlined"
            fullWidth
            value={novoGrupo.nome}
            onChange={(e) => setNovoGrupo({ ...novoGrupo, nome: e.target.value })}
          />
          <TextField
            label="IDs dos Participantes (separados por vírgula)"
            variant="outlined"
            fullWidth
            onChange={(e) => {
              const ids = e.target.value.split(",").map((id) => ({ idEgresso: parseInt(id.trim(), 10) }));
              setNovoGrupo({ ...novoGrupo, participantes: ids });
            }}
          />
          <Button
            variant="contained"
            sx={{ bgcolor: "#4CAF50", "&:hover": { bgcolor: "#388E3C" } }}
            onClick={criarGrupo}
          >
            Criar Grupo
          </Button>
        </Box>
      )}

      {/* Lista de Grupos */}
      {grupos.length === 0 && (
        <Typography variant="h6" sx={{ textAlign: "center", mt: 5, color: "#2E7D32" }}>
          Nenhum grupo encontrado
        </Typography>
      )}
      {grupos.map((grupo) => (
        <Paper
          key={grupo.id}
          sx={{
            p: 2,
            backgroundColor: "#4CAF50",
            color: "white",
            borderRadius: 3,
            cursor: "pointer",
            textAlign: "center",
            "&:hover": { backgroundColor: "#388E3C" },
          }}
          onClick={() => entrarNoChat(grupo.id, grupo.participantes, grupo.criador)} // Verifica se o usuário é membro antes de navegar
        >
          <Typography variant="h6">{grupo.nome}</Typography>
        </Paper>
      ))}
    </Box>
  );
}
