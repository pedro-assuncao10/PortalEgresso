import React, { useEffect, useState } from "react"; 
import { Container, Paper, Typography, Box, IconButton, Drawer, Button, TextField, Card,FormControl, InputLabel, Select, MenuItem, Checkbox, ListItemText, } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";  // Importa o ícone de voltar
import CloseIcon from "@mui/icons-material/Close";
import axios from "axios";
import { data, useNavigate } from "react-router-dom";

export default function Vagas() {
    const [vagas, setVagas] = useState([]);
    const [loading, setLoading] = useState(true);
    const [menuAberto, setMenuAberto] = useState(false);
    const [novaVaga, setNovaVaga] = useState("");
    const [painelAberto, setPainelAberto] = useState(false);
    const [modoEdicao, setModoEdicao] = useState(false);
    const [idVagaEdicao, setIdVagaEdicao] = useState(null);
    const [vagasEgresso, setVagasEgresso] = useState([]);
    const [idCoordenador, setIdCoordenador] = useState(null);
    const [painelSelecaoAberto, setPainelSelecaoAberto] = useState(false);
    const navigate = useNavigate();
    const [tituloVaga, setTituloVaga] = useState("");
    const [tipoEstagio, setTipoEstagio] = useState("");
    const [localidade, setLocalidade] = useState("");
    const [dataPublicacao, setDataPublicacao] = useState("");
    const [cursosSelecionados, setCursosSelecionados] = useState([]);
    const [cursosDisponiveis, setCursosDisponiveis] = useState([]);
    // Função para verificar o tipo de usuário no localStorage
    const verificarTipoUsuario = () => {
      const usuario = JSON.parse(localStorage.getItem("usuario"));
      return usuario ? usuario.tipo : null; // Retorna o tipo de usuário ou null se não houver usuário
    };
    
    const BASE_URL = "https://merry-amazement-production.up.railway.app";

    useEffect(() => {
    const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));
    if (!usuarioLogado) {
      navigate("/login");
      return;
    }

    setIdCoordenador(usuarioLogado.idCoordenador);

    async function carregarVagas() {
        try {
          const resposta = await axios.get(`${BASE_URL}/api/vagas`);
          setVagas(resposta.data);
        } catch (error) {
          console.error("Erro ao buscar vagas", error);
        } finally {
          setLoading(false);
        }
      }
      carregarVagas();
    }, []);
  
    useEffect(() => {
      async function pegarVagasEgresso() {
        if (idCoordenador) {
          try {
            const resposta = await axios.get(`${BASE_URL}/api/vagas/porEgresso/${idCoordenador}`);
            setVagasEgresso(resposta.data);
          } catch (error) {
            console.error("Erro ao buscar vagas do egresso", error);
          }
        }
      }
      pegarVagasEgresso();
    }, [idCoordenador]);

    const abrirPainelEdicao = (id, descricao) => {
        setModoEdicao(true);
        setPainelAberto(true);
        setIdVagaEdicao(id);
        setNovaVaga(descricao);
    };
    
    const abrirPainelSelecao = () => {
        setPainelSelecaoAberto(true);
    };  

    const fecharPainelSelecao = () => {
        setPainelSelecaoAberto(false);
    }; 
    

    const criarVaga = async () => {
        if (!novaVaga.trim()) {
          alert("Digite uma descrição para a vaga!");
          return;
        }
      
        try {
          const vaga = {
            titulo: tituloVaga, // Certifique-se de ter um estado para 'tituloVaga'
            descricao: novaVaga,
            tipoEstagio: tipoEstagio, // Adicione um estado para 'tipoEstagio'
            localidade: localidade, // Adicione um estado para 'localidade'
            dataPublicacao: new Date().toISOString().split("T")[0], // Data atual formatada
            egresso: {
              idEgresso: idCoordenador, //coordenador esteja publicando a vaga
            },
            cursosDestinados: cursosSelecionados.map((id) => ({ idCurso: id })), // Certifique-se de ter um array com os cursos
          };
      
          await axios.post(`${BASE_URL}/api/vagas`, vaga);
      
          setNovaVaga("");
          setTituloVaga(""); // Limpa o campo do título
          setTipoEstagio(""); // Limpa o campo do tipo de estágio
          setLocalidade(""); // Limpa o campo da localidade
          setCursosSelecionados([]); // Limpa os cursos selecionados
          setPainelAberto(false);
          window.location.reload();
        } catch (error) {
          console.error("Erro ao criar vaga", error);
        }
    };
      
    
    const editarVaga = async () => {
        if (!novaVaga.trim()) {
          alert("Digite uma descrição para a vaga!");
          return;
        }
      
        try {
          const vagaAtualizada = {
            titulo: tituloVaga, // Certifique-se de ter um estado para 'tituloVaga'
            descricao: novaVaga,
            tipoEstagio: tipoEstagio, // Estado para 'tipoEstagio'
            localidade: localidade, // Estado para 'localidade'
            dataPublicacao: dataPublicacao || new Date().toISOString().split("T")[0], // Mantém a data existente ou define a atual
            egresso: {
              idEgresso: idCoordenador, // Assumindo que o coordenador está editando a vaga
            },
            cursosDestinados: cursosSelecionados.map((id) => ({ idCurso: id })), // Converte array de IDs em objetos esperados
          };
      
          await axios.put(`${BASE_URL}/api/vagas/${idVagaEdicao}`, vagaAtualizada);
      
          setPainelAberto(false);
          setModoEdicao(false);
          window.location.reload();
        } catch (error) {
          console.error("Erro ao editar vaga", error);
        }
      };
      
    
      const deletarVaga = async (idVaga) => {
        try {
          await axios.delete(`${BASE_URL}/api/vagas/${idVaga}`);
          window.location.reload();
        } catch (error) {
          console.error("Erro ao deletar vaga", error);
        }
      };    
      
      const carregarCursos = async () => {
        try {
          const response = await axios.get(`${BASE_URL}/api/cursos`);
          setCursosDisponiveis(response.data); // Supondo que o backend retorna uma lista de cursos
        } catch (error) {
          console.error("Erro ao carregar cursos", error);
        }
      };
      
      // Chamada automática ao montar o componente
      useEffect(() => {
        carregarCursos();
      }, []);
      

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
          Painel de Vagas
        </Typography>
        <IconButton sx={{ position: "absolute", right: 0 }} onClick={() => setMenuAberto(true)}>
          <MenuIcon />
        </IconButton>
      </Box>

        {vagas.length === 0 ? (
            <Typography align="center">Nenhuma Vaga de Estágio encontrada!</Typography>
        ) : (
        vagas.map((vaga) => (
            <Box
            key={vaga.idVaga}
            sx={{
                mb: 4,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                width: "100%",
            }}
            >
            <Paper
                sx={{
                backgroundColor: "#4CAF50",
                color: "white",
                borderRadius: "20px",
                padding: "20px",
                width: "90%",
                minHeight: "200px",
                textAlign: "left",
                fontSize: "16px",
                }}
            >
                {/* Nome do Egresso */}
                <Typography variant="h6" fontWeight="bold" mb={1}>
                {vaga.egresso.nome} - {vaga.egresso.descricao}
                </Typography>

                {/* Título da Vaga */}
                <Typography fontSize="18px" fontWeight="bold" mb={1}>
                {vaga.titulo}
                </Typography>

                {/* Descrição da Vaga */}
                <Typography fontSize="16px" mb={2}>
                {vaga.descricao}
                </Typography>

                {/* Informações Adicionais */}
                <Typography><strong>Tipo:</strong> {vaga.tipoEstagio}</Typography>
                <Typography><strong>Localidade:</strong> {vaga.localidade}</Typography>
                <Typography><strong>Data de Publicação:</strong> {vaga.dataPublicacao}</Typography>

                {/* Cursos Destinados */}
                {vaga.cursosDestinados.length > 0 && (
                <Typography><strong>Cursos Destinados:</strong> {vaga.cursosDestinados.map((curso) => curso.nome).join(", ")}</Typography>
                )}

                {/* Botão de Chat */}
                <Box sx={{ mt: 2 }}>
                <Button
                    variant="contained"
                    sx={{
                    bgcolor: "white",
                    color: "#4CAF50",
                    borderRadius: 2,
                    fontSize: "12px",
                    minWidth: "100px",
                    }}
                >
                    Candidatar-se
                </Button>
                </Box>
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
            Criar Nova Vaga de estagio
          </Button>
          <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={abrirPainelSelecao}>
            Editar Vaga de estagio
          </Button>
          <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={abrirPainelSelecao}>
            Deletar Vaga de estagio
          </Button>
        </Box>
      </Drawer>
      {painelSelecaoAberto && vagasEgresso.length > 0 && (
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
            Selecione a Vaga para Editar ou Deletar
            </Typography>

            {vagasEgresso.map((vaga) => (
            <Paper
                key={vaga.idVaga}
                sx={{
                p: 2,
                mb: 2,
                bgcolor: "#4CAF50",
                color: "white",
                cursor: "pointer",
                textAlign: "center",
                }}
                onClick={() => {
                abrirPainelEdicao(
                    vaga.idVaga,
                    vaga.titulo,
                    vaga.descricao,
                    vaga.tipoEstagio,
                    vaga.localidade,
                    vaga.dataPublicacao,
                    vaga.egresso.idEgresso,
                    vaga.cursosDestinados.map((curso) => curso.idCurso)
                );
                setPainelSelecaoAberto(false);
                }}
                onDoubleClick={() => {
                if (window.confirm("Tem certeza que deseja deletar esta vaga?")) {
                    deletarVaga(vaga.idVaga);
                    setPainelSelecaoAberto(false);
                }
                }}                 
            >
                {vaga.titulo}
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

      {/* Painel para Nova/Editar Vaga */}
        {painelAberto && (
        <Card
            sx={{
            p: 3,
            bgcolor: "#4CAF50",
            borderRadius: 3,
            width: "100%",
            maxWidth: 400,
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            zIndex: 1000,
            }}
        >
            <Typography color="white" fontWeight="bold" mb={2}>
            {modoEdicao ? "Editar Vaga" : "Nova Vaga"}
            </Typography>

            {/* Campo Título */}
            <TextField
            fullWidth
            label="Título"
            value={tituloVaga}
            onChange={(e) => setTituloVaga(e.target.value)}
            sx={{ mb: 2, bgcolor: "white" }}
            />

            {/* Campo Descrição */}
            <TextField
            fullWidth
            label="Descrição"
            multiline
            rows={4}
            value={novaVaga}
            onChange={(e) => setNovaVaga(e.target.value)}
            sx={{ mb: 2, bgcolor: "white" }}
            />

            {/* Campo Tipo de Estágio */}
            <TextField
            fullWidth
            label="Tipo de Estágio"
            value={tipoEstagio}
            onChange={(e) => setTipoEstagio(e.target.value)}
            sx={{ mb: 2, bgcolor: "white" }}
            />

            {/* Campo Localidade */}
            <TextField
            fullWidth
            label="Localidade"
            value={localidade}
            onChange={(e) => setLocalidade(e.target.value)}
            sx={{ mb: 2, bgcolor: "white" }}
            />

            {/* Campo Data de Publicação */}
            <TextField
            fullWidth
            label="Data de Publicação"
            type="date"
            value={dataPublicacao}
            onChange={(e) => setDataPublicacao(e.target.value)}
            sx={{ mb: 2, bgcolor: "white" }}
            InputLabelProps={{ shrink: true }}
            />

            {/* Campo Cursos Destinados */}
            <FormControl fullWidth sx={{ mb: 2, bgcolor: "white" }}>
            <InputLabel id="cursos-label">Cursos Destinados</InputLabel>
            <Select
                labelId="cursos-label"
                multiple
                value={cursosSelecionados}
                onChange={(e) => setCursosSelecionados(e.target.value)}
                renderValue={(selected) =>
                cursosDisponiveis
                    .filter((curso) => selected.includes(curso.idCurso))
                    .map((curso) => curso.nome)
                    .join(", ")
                }
            >
                {cursosDisponiveis.map((curso) => (
                <MenuItem key={curso.idCurso} value={curso.idCurso}>
                    <Checkbox checked={cursosSelecionados.includes(curso.idCurso)} />
                    <ListItemText primary={curso.nome} />
                </MenuItem>
                ))}
            </Select>
            </FormControl>

            {/* Botões */}
            <Box display="flex" justifyContent="space-between" mt={3}>
            <Button
                variant="contained"
                sx={{ bgcolor: "#388E3C", mt: 2 }}
                onClick={() => {
                if (modoEdicao) {
                    editarVaga();
                } else {
                    criarVaga();
                }
                }}
            >
                {modoEdicao ? "Salvar" : "Cadastrar"}
            </Button>
            <Button
                variant="outlined"
                sx={{ color: "white", borderColor: "white" }}
                onClick={() => setPainelAberto(false)}
            >
                Cancelar
            </Button>
            </Box>
        </Card>
        )}
    </Container>
  );

}
