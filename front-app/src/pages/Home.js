import { useState, useEffect } from "react";
import axios from "axios";
import { Box, Card, Typography, Avatar, Button, Drawer, IconButton, Paper } from "@mui/material";
import { Menu as MenuIcon, Close as CloseIcon } from "@mui/icons-material"; 
import ArrowBackIcon from "@mui/icons-material/ArrowBack";  // Importa o ícone de voltar
import { PieChart, Pie, BarChart, Bar, XAxis, YAxis, Tooltip, Legend } from "recharts";
import { useNavigate } from "react-router-dom";
import TextField from '@mui/material/TextField';


const Home = () => {
  const navigate = useNavigate();
  const [menuAberto, setMenuAberto] = useState(false);
  const [cargo, setCargo] = useState({ descricao: "", local: "", anoInicio: "", anoFim: "" });
  const [painelSelecaoAberto, setPainelSelecaoAberto] = useState(false);
  const [cargosEgresso, setCargosEgresso] = useState([]);
  const [cargoSelecionado, setCargoSelecionado] = useState(null);
  const [modoEdicao, setModoEdicao] = useState(false);
  const [painelAberto, setPainelAberto] = useState(false);
  const [cursoSelecionado, setCursoSelecionado] = useState(null);
  const fecharPainelSelecaoCurso = () => setPainelSelecaoAbertoCurso(false);

  const [painelSelecaoAbertoCurso, setPainelSelecaoAbertoCurso] = useState(false);
  const [painelAbertoCurso, setPainelAbertoCurso] = useState(false);
  const [curso, setCurso] = useState({ idCurso: "", nome: "", nivel: "", idCoordenador: "" });
  const [cursos, setCursos] = useState([]);

  const [painelSelecaoAbertoEgresso, setPainelSelecaoAbertoEgresso] = useState(false);
  const [painelAbertoEgresso, setPainelAbertoEgresso] = useState(false);
  const [egresso, setEgresso] = useState({ nome: "", email: "", descricao: "", foto: "", linkedin: "", instagam: "", curriculo: "", idCurso: "", anoInicio: "", anoFim: "" });
  const [egressos, setEgressos] = useState([]);

  useEffect(() => {
    const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));
    if (!usuarioLogado) {
      navigate("/login");
      return;
    }
    console.log(usuarioLogado);

    const idCoordenador = usuarioLogado.idCoordenador; // Pega o ID salvo no login
    console.log(idCoordenador);
    // Buscar os dados do egresso
    axios.get(`http://localhost:8080/api/egressos/${idCoordenador}`)
      .then(response => {
        if (!response.data) {
          throw new Error("Egresso não encontrado.");
        }
        setEgresso(response.data); // Atualiza o estado corretamente
        // Buscar os dados do cargo associado ao egresso
        return axios.get(`http://localhost:8080/api/cargos/porEgresso/${idCoordenador}`);
      })
      .then(response => {
        if (response.data.length > 0) {
          setCargo(response.data[0]); // Define o primeiro cargo retornado
        }
      })
      .catch(error => {
        console.error("Erro ao buscar dados do egresso:", error);
      });
      // Buscar o depoimento do egresso${idCoordenador}
      axios.get(`http://localhost:8080/api/depoimentos/porEgresso/${idCoordenador}`)
      .then(response => {
        if (response.data.length > 0) {//verifica se há depoimentos
          setEgresso(prevState => ({ ...prevState, texto : response.data[0].texto }));
        }
      })
      .catch(error => {
        console.error("Erro ao buscar depoimento:", error);
      });
  
  }, [navigate]);

  useEffect(() => {
    if (egresso?.idEgresso) {
      axios.get(`http://localhost:8080/api/cargos/porEgresso/${egresso.idEgresso}`)
        .then(response => {
          setCargosEgresso(response.data);
        })
        .catch(error => {
          console.error("Erro ao buscar cargos do egresso:", error);
        });
    }
  }, [egresso]);
  
  //cargo
  const handleChange = (e) => {
    setCargo({ ...cargo, [e.target.name]: e.target.value });
  };

  // Função para abrir o painel de seleção de cargos
  const abrirPainelSelecao = () => {
    setPainelSelecaoAberto(true);
  };

  // Função para fechar o painel de seleção
  const fecharPainelSelecao = () => {
    setPainelSelecaoAberto(false);
  };

  // Função para abrir o painel de edição do cargo
  const abrirPainelEdicao = (idCargo, nomeCargo) => {
    const cargoParaEditar = cargosEgresso.find(c => c.idCargo === idCargo);
    
    if (cargoParaEditar) {
      setCargoSelecionado(cargoParaEditar);
      setCargo({
        descricao: cargoParaEditar.descricao,
        local: cargoParaEditar.local,
        anoInicio: cargoParaEditar.anoInicio,
        anoFim: cargoParaEditar.anoFim
      });
      setModoEdicao(true);
      setPainelAberto(true);
    }
  };
  
  const criarCargo = () => {
    const novoCargo = { ...cargo, idEgresso: egresso?.idEgresso };
    axios.post("http://localhost:8080/api/cargos/criar", novoCargo)
      .then(response => {
        alert("Cargo criado com sucesso!");
      })
      .catch(error => {
        console.error("Erro ao criar cargo:", error);
      });
  };

  const editarCargo = () => {
    if (!cargoSelecionado) return;
  
    const cargoEditado = { 
      ...cargo, 
      idCargo: cargoSelecionado.idCargo 
    };
  
    axios.put(`http://localhost:8080/api/cargos/atualizar`, cargoEditado) // Removido o ID da URL
      .then(response => {
        setPainelAberto(false);
        setModoEdicao(false);
        setCargoSelecionado(null);
        alert("Cargo atualizado com sucesso!");
      })
      .catch(error => {
        console.error("Erro ao editar cargo:", error);
      });
  };
  
  

  const deletarCargo = (idCargo) => {
    console.log(idCargo);
    axios.delete("http://localhost:8080/api/cargos/deletar", {
      data: { idCargo: idCargo } // Corpo da requisição
    })
    .then(response => {
      alert("Cargo deletado com sucesso!");
      setCargosEgresso(cargosEgresso.filter(c => c.idCargo !== idCargo));
    })
    .catch(error => {
      console.error("Erro ao deletar cargo:", error);
    });
  };
  

  const handleCargoClick = (idCargo) => {
    let clickTimer = null;
  
    if (clickTimer) {
      clearTimeout(clickTimer);
      clickTimer = null;
      if (window.confirm("Tem certeza que deseja deletar este cargo?")) {
        deletarCargo(idCargo);
        setPainelSelecaoAberto(false);
      }
    } else {
      clickTimer = setTimeout(() => {
        abrirPainelEdicao(idCargo);
        clickTimer = null;
      }, 300); // Tempo para diferenciar clique simples de duplo
    }
  };

  //curso e egresso
  const API_URL = "http://localhost:8080/api/cursos"; // Ajuste conforme necessário

  // Função para abrir o painel de edição
  const abrirPainelEdicaoCurso = (idCurso) => {
    const cursoParaEditar = cursos.find((c) => c.idCurso === idCurso);
    
    if (cursoParaEditar) {
      setCursoSelecionado(cursoParaEditar);
      setCurso({
        idCurso: cursoParaEditar.idCurso, // Adicionando ID para evitar perda de referência
        nome: cursoParaEditar.nome,
        nivel: cursoParaEditar.nivel,
        idCoordenador: cursoParaEditar.idCoordenador,
      });
      setModoEdicao(true);
      setPainelAbertoCurso(true);
    } else {
      alert("Erro ao selecionar o curso para edição.");
    }
  };
  

  // Função para criar um novo curso
  const criarCurso = () => {
    const novoCurso = { ...curso };
    axios.post(API_URL, novoCurso)
      .then(response => {
        alert("Curso criado com sucesso!");
        setCursos([...cursos, response.data]); // Adiciona o novo curso à lista
        setPainelAbertoCurso(false);
      })
      .catch(error => {
        console.error("Erro ao criar curso:", error);
      });
  };

  // Função para editar um curso
  const editarCurso = () => {
    if (!cursoSelecionado) return;

    const cursoEditado = {
      ...curso,
      idCurso: cursoSelecionado.idCurso,
    };

    axios.put(API_URL, cursoEditado)
      .then(response => {
        setModoEdicao(false);
        setCursoSelecionado(null);
        setCursos(cursos.map(c => (c.idCurso === cursoEditado.idCurso ? response.data : c))); // Atualiza o curso na lista
        
        alert("Curso atualizado com sucesso!");
      })
      .catch(error => {
        console.error("Erro ao editar curso:", error);
      });
  };

  // Função para deletar um curso
  const deletarCurso = (idCurso) => {
    if (window.confirm("Tem certeza que deseja deletar este curso?")) {
      axios
        .delete(`${API_URL}/${idCurso}`)
        .then(() => {
          alert("Curso deletado com sucesso!");
          setCursos((cursos) => cursos.filter((c) => c.idCurso !== idCurso));
          setPainelSelecaoAbertoCurso(false); // Fechar painel após ação
        })
        .catch((error) => {
          console.error("Erro ao deletar curso:", error);
          alert("Erro ao deletar o curso. Verifique o console para mais detalhes.");
        });
    }
  };

  useEffect(() => {
    listarCursos(); // Carrega cursos ao montar o componente
  }, []);

  // Função para listar cursos
  const listarCursos = () => {
    axios
      .get(API_URL)
      .then((response) => {
        setCursos(response.data); // Atualiza os cursos no estado
      })
      .catch((error) => {
        console.error("Erro ao listar cursos:", error);
        alert("Erro ao listar cursos. Verifique o console.");
      });
  };

  // Estado para o painel de listagem de cursos
  const [painelListarCursosAberto, setPainelListarCursosAberto] = useState(false);

  // Função para abrir o painel e carregar os cursos
  const abrirPainelListarCursos = () => {
    listarCursos(); // Atualiza a lista de cursos ao abrir o painel
    setPainelListarCursosAberto(true);
  };

  // Função para fechar o painel de listagem de cursos
  const fecharPainelListarCursos = () => {
    setPainelListarCursosAberto(false);
  };

  const editarEgresso = async () => {
    if (!egresso.idEgresso) {
      alert("Erro: ID do egresso não encontrado.");
      return;
    }
  
    try {
      const url = `http://localhost:8080/api/egressos/${egresso.idEgresso}?idCurso=${egresso.idCurso || 0}&anoInicio=${egresso.anoInicio || 0}&anoFim=${egresso.anoFim || ""}`;
  
      const response = await fetch(url, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json",
        },
        body: JSON.stringify({
          idEgresso: egresso.idEgresso, 
          nome: egresso.nome?.trim() || "",
          email: egresso.email?.trim() || "",
          descricao: egresso.descricao?.trim() || "",
          foto: egresso.foto?.trim() || "",
          linkedin: egresso.linkedin?.trim() || "",
          instagram: egresso.instagram?.trim() || "",
          curriculo: egresso.curriculo?.trim() || "",
        }),
      });
  
      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Erro ${response.status}: ${errorMessage}`);
      }
  
      alert("Egresso editado com sucesso!");
      setPainelAbertoEgresso(false); // Fecha o painel após sucesso
    } catch (error) {
      console.error("Erro ao editar egresso:", error);
      alert("Erro ao editar egresso: " + error.message);
    }
  };
  
  
  const deletarEgresso = async () => {
      if (!egresso.idEgresso) {
      alert("Erro: ID do egresso não encontrado.");
      return;
    }
  
    if (!window.confirm("Tem certeza que deseja deletar sua conta? Essa ação não pode ser desfeita.")) {
      return;
    }
  
    try {
      const response = await fetch(`http://localhost:8080/api/egressos/${egresso.idEgresso}`, {
        method: "DELETE",
      });
  
      if (!response.ok) {
        throw new Error(`Erro ${response.status}: ${await response.text()}`);
      }
  
      alert("Egresso deletado com sucesso!");
      navigate("/login"); // Redireciona para login após deletar conta
    } catch (error) {
      console.error("Erro ao deletar egresso:", error);
      alert("Erro ao deletar egresso: " + error.message);
    }
  };
  

  const handleCursoClick = (idCurso) => {
    const cursoSelecionado = cursos.find((c) => c.idCurso === idCurso);
    setCurso(cursoSelecionado);
    setPainelAbertoCurso(true);
  };

  const handleEgressoClick = (nome) => {
    const egressoSelecionado = egressos.find((e) => e.nome === nome);
    setEgresso(egressoSelecionado);
    setPainelAbertoEgresso(true);
  };
  
  return (
    <Box display="flex" flexDirection="column" alignItems="center" p={3}>
      
      {/* Barra de Navegação */}
      <Box
        sx={{
          position: "fixed",
          top: 0,
          width: "930px",
          backgroundColor: "#2E7D32",
          padding: "10px",
          display: "flex",
          justifyContent: "center",
          alignItems: "flex-start",
          gap: "20px",
          borderRadius: "0px 0px 20px 20px",
          zIndex: 1000
        }}
      >
        <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={() => navigate("/depoimentos")}>
          Depoimentos
        </Button>
        <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={() => navigate("/vagas")}>
          Painel de Estágio
        </Button>
        <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={() => navigate("/grupos")}>
          Grupo de Discussão
        </Button>
        <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={() => navigate("/graficoCargos")}>
          dados dos egressos
        </Button>
        {/* Ícone de voltar */}
        <IconButton sx={{ position: "absolute", left: 0 }} onClick={() => navigate("/")}>
          <ArrowBackIcon />
        </IconButton>
        {/* Ícone do Menu Lateral */}
        <IconButton
          sx={{ position: "absolute", right: 20, color: "white" }}
          onClick={() => setMenuAberto(true)}
        >
          <MenuIcon />
        </IconButton>
      </Box>

      {/* Menu Lateral */}
      <Drawer anchor="right" open={menuAberto} onClose={() => setMenuAberto(false)}>
        <Box sx={{ width: 250, p: 2, display: "flex", flexDirection: "column", gap: 2 }}>
          {/* Botão de Fechar */}
          <IconButton sx={{ alignSelf: "flex-end" }} onClick={() => setMenuAberto(false)}>
            <CloseIcon />
          </IconButton>

          {/* Botões do Menu */}
          <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={() => setPainelAbertoEgresso(true)}>Editar dados</Button>
          {/* Botão para deletar egresso */}
          <Button
            variant="contained"
            sx={{ bgcolor: "#4CAF50", color: "white" }}
            onClick={deletarEgresso}
          >
            Deletar Conta
          </Button>
          <Button variant="contained" sx={{ bgcolor: "#4CAF50" }} onClick={() => window.location.href = '/'}>Sair da conta</Button>
          <Box sx={{ width: "100%", height: "1px", bgcolor: "gray", my: 1 }} />
        </Box>
      </Drawer>


      {/* Conteúdo principal */}
      <Box display="flex" justifyContent="space-between"flexDirection="column" alignItems="flex-start" p={3} mt={10} style={{ paddingTop: "60px" }} >

        {/* Perfil + Cargo + Ações */}
        <Box display="flex" width="100%" justifyContent="space-between" gap={3} mb={3}>

          {/* Balão de Perfil */}
          <Card sx={{ p: 2, display: "flex", flexDirection: "column", alignItems: "flex", borderRadius: 3, bgcolor: "#4CAF50", minWidth: 250, minHeight: 180 }}>
            <Avatar src={egresso?.foto} sx={{ width: 80, height: 80, mb: 1 }} />
            <Typography color="white" fontWeight="bold">{egresso ? egresso.nome : "Carregando..."}</Typography>
            <Typography color="white">{egresso?.email || "Email não informado"}</Typography>
            <Typography color="white">{egresso?.linkedin ? `LinkedIn: ${egresso.linkedin}` : "Sem LinkedIn"}</Typography>
            <Typography color="white">{egresso?.instagram ? `Instagram: ${egresso.instagram}` : "Sem Instagram"}</Typography>
            <Typography color="white">{egresso?.curriculo ? <a href={egresso.curriculo} style={{ color: "white" }}>Currículo</a> : "Sem currículo"}</Typography>
          </Card>

          {/* Balão de Cargo + Botões */}
          <Box display="flex" alignItems="center" gap={1}>

            {/* Balão de Cargo */}
            <Card sx={{ p: 10, borderRadius: 3, bgcolor: "#4CAF50", height: 100, display: "flex", alignItems: "flex",flexDirection: "column", justifyContent: "center" }}>
              <Typography color="white">Cargo: {cargo?.descricao || "Não informado"}</Typography>
              <Typography color="white">Local: {cargo?.local || "Não informado"}</Typography>
              <Typography color="white">Ano de Início: {cargo?.anoInicio || "Não informado"}</Typography>
              <Typography color="white">Ano de Fim: {cargo?.anoFim || "Não informado"}</Typography>
            </Card>

            {/* Botões de Ação */}
            <Box display="flex" flexDirection="column" justifyContent="space-between" height={100} gap={1}>
              <Button 
                onClick={() => {
                  setModoEdicao(false);  // Garante que estamos criando, não editando
                  setCargo({ descricao: "", local: "", anoInicio: "", anoFim: "" }); // Reseta o formulário
                  setPainelAberto(true);
                }} 
                variant="contained" 
                sx={{ bgcolor: "#4CAF50", borderRadius: 2, fontSize: "12px", flex: 1, minWidth: "100px" }}
              >
                Criar
              </Button>
              <Button
                onClick={() => {
                    setModoEdicao(true);  // Ativa o modo de edição
                    abrirPainelSelecao();  // Abre o painel de seleção
                    console.log('Modo de Edição Ativado', modoEdicao);  // Verifica se o modo foi ativado corretamente
                }}
                variant="contained"
                sx={{ bgcolor: "#4CAF50", borderRadius: 2, fontSize: "12px", flex: 1, minWidth: "100px" }}
                >
                Editar
                </Button>

                <Button
                onClick={() => {
                    setModoEdicao(false);  // Desativa o modo de edição
                    abrirPainelSelecao();  // Abre o painel de seleção
                    console.log('Modo de Edição Desativado', modoEdicao);  // Verifica se o modo foi desativado corretamente
                }}
                variant="contained"
                sx={{ bgcolor: "#4CAF50", borderRadius: 2, fontSize: "12px", flex: 1, minWidth: "100px" }}
                >
                Deletar
                </Button>
            </Box>
            {painelSelecaoAberto && cargosEgresso.length > 0 && (
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
                            Selecione o Cargo para Editar ou Deletar
                            </Typography>
                            {cargosEgresso.map((cargo) => (
                            <Paper
                                key={cargo.idCargo}
                                sx={{
                                p: 2,
                                mb: 2,
                                bgcolor: "#4CAF50",
                                color: "white",
                                cursor: "pointer",
                                textAlign: "center",
                                }}
                                onClick={() => {
                                console.log('Modo de Edição:', modoEdicao);
                                // Se estiver em modo de edição, abre o painel de edição
                                if (modoEdicao) {
                                    abrirPainelEdicao(cargo.idCargo);
                                    setPainelSelecaoAberto(false);  // Fechar o painel após abrir o painel de edição
                                } else {
                                    // Caso contrário, pede confirmação para deletar o cargo
                                    console.log("ID do Cargo selecionado para deleção:", cargo.idCargo);
                                    if (window.confirm("Tem certeza que deseja deletar este cargo?")) {
                                    deletarCargo(cargo.idCargo);
                                    setPainelSelecaoAberto(false);  // Fechar o painel após deletar o cargo
                                    }
                                }
                                }}
                            >
                                {cargo.descricao}
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

            {/* Painel para Nova/Editar Cargo */}
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
                  {cargo.idCargo ? "Editar Cargo" : "Novo Cargo"}
                </Typography>

                {/* Campo Descrição */}
                <TextField
                  fullWidth
                  label="Descrição"
                  name="descricao"
                  value={cargo.descricao}
                  onChange={handleChange}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                {/* Campo Local */}
                <TextField
                  fullWidth
                  label="Local"
                  name="local"
                  value={cargo.local}
                  onChange={handleChange}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                {/* Campo Ano de Início */}
                <TextField
                  fullWidth
                  label="Ano de Início"
                  name="anoInicio"
                  type="number"
                  value={cargo.anoInicio}
                  onChange={handleChange}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                {/* Campo Ano de Fim */}
                <TextField
                  fullWidth
                  label="Ano de Fim"
                  name="anoFim"
                  type="number"
                  value={cargo.anoFim}
                  onChange={handleChange}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                {/* Botões */}
                <Box display="flex" justifyContent="space-between" mt={3}>
                  <Button
                    variant="contained"
                    sx={{ bgcolor: "#388E3C", mt: 2 }}
                    onClick={() => {
                      if (modoEdicao) {
                        editarCargo(); // Caso o cargo já tenha id, edita o cargo
                      } else {
                        criarCargo(); // Caso não tenha id, cria um novo cargo
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
            {/* Painel de Seleção de Curso */}
            {painelSelecaoAbertoCurso && cursos.length > 0 && (
              <Card sx={{ p: 3, bgcolor: "#A5D6A7", borderRadius: 3, width: "100%", maxWidth: 400, position: "absolute", top: "50%", left: "50%", transform: "translate(-50%, -50%)", zIndex: 1000, overflowY: "auto", maxHeight: "400px" }}>
                <Typography color="black" fontWeight="bold" mb={2}>Selecione o Curso para Criar, Editar ou Deletar</Typography>
                {cursos.map((curso) => (
                  <Paper key={curso.idCurso} sx={{ p: 2, mb: 2, bgcolor: "#4CAF50", color: "white", cursor: "pointer", textAlign: "center" }} onClick={() => { if (modoEdicao) { abrirPainelEdicaoCurso(curso.idCurso, curso.nome); setPainelSelecaoAbertoCurso(false); } }} onDoubleClick={() => { if (!modoEdicao) { if (window.confirm("Tem certeza que deseja deletar este curso?")) { deletarCurso(curso.idCurso); setPainelSelecaoAbertoCurso(false); } } }}>
                    {curso.nome}
                  </Paper>
                ))}
                <Button variant="outlined" sx={{ color: "black", borderColor: "black", mt: 2 }} onClick={fecharPainelSelecaoCurso}>Fechar</Button>
              </Card>
            )}
            
            {/* Painel para Novo/Editar Curso */}
            {painelAbertoCurso && (
              <Card sx={{ p: 3, bgcolor: "#4CAF50", borderRadius: 3, width: "100%", maxWidth: 400, position: "absolute", top: "50%", left: "50%", transform: "translate(-50%, -50%)", zIndex: 1000 }}>
                <Typography color="white" fontWeight="bold" mb={2}>{modoEdicao ? "Editar Curso" : "Novo Curso"}</Typography>
                <TextField fullWidth label="Nome" name="nome" value={curso.nome} onChange={(e) => setCurso({ ...curso, nome: e.target.value })} sx={{ mb: 2, bgcolor: "white" }} />
                <TextField fullWidth label="Nível" name="nivel" value={curso.nivel} onChange={(e) => setCurso({ ...curso, nivel: e.target.value })} sx={{ mb: 2, bgcolor: "white" }} />
                <TextField fullWidth label="ID Coordenador" name="idCoordenador" value={curso.idCoordenador} onChange={(e) => setCurso({ ...curso, idCoordenador: e.target.value })} sx={{ mb: 2, bgcolor: "white" }} />
                <Box display="flex" justifyContent="space-between" mt={3}>
                  <Button variant="contained" sx={{ bgcolor: "#388E3C", mt: 2 }} onClick={() => { if (modoEdicao) { editarCurso(); } else { criarCurso(); } }}>{modoEdicao ? "Salvar" : "Cadastrar"}</Button>
                  <Button variant="outlined" sx={{ color: "white", borderColor: "white" }} onClick={() => setPainelAbertoCurso(false)}>Cancelar</Button>
                </Box>
              </Card>
            )}
            
            {/* Painel para Novo/Editar Egresso */}
            {painelAbertoEgresso && (
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
                  {modoEdicao ? "Editar Egresso" : "Novo Egresso"}
                </Typography>

                <TextField
                  fullWidth
                  label="Nome"
                  name="nome"
                  value={egresso.nome}
                  onChange={(e) => setEgresso({ ...egresso, nome: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="Email"
                  name="email"
                  value={egresso.email}
                  onChange={(e) => setEgresso({ ...egresso, email: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="Descrição"
                  name="descricao"
                  value={egresso.descricao}
                  onChange={(e) => setEgresso({ ...egresso, descricao: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="Foto URL"
                  name="foto"
                  value={egresso.foto}
                  onChange={(e) => setEgresso({ ...egresso, foto: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="LinkedIn"
                  name="linkedin"
                  value={egresso.linkedin}
                  onChange={(e) => setEgresso({ ...egresso, linkedin: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="Instagram"
                  name="instagram"
                  value={egresso.instagram}
                  onChange={(e) => setEgresso({ ...egresso, instagram: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="Currículo URL"
                  name="curriculo"
                  value={egresso.curriculo}
                  onChange={(e) => setEgresso({ ...egresso, curriculo: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="Ano de Início"
                  name="anoInicio"
                  type="number"
                  value={egresso.anoInicio}
                  onChange={(e) => setEgresso({ ...egresso, anoInicio: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="Ano de Fim"
                  name="anoFim"
                  type="number"
                  value={egresso.anoFim}
                  onChange={(e) => setEgresso({ ...egresso, anoFim: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <TextField
                  fullWidth
                  label="ID do Curso"
                  name="idCurso"
                  type="number"
                  value={egresso.idCurso}
                  onChange={(e) => setEgresso({ ...egresso, idCurso: e.target.value })}
                  sx={{ mb: 2, bgcolor: "white" }}
                />

                <Box display="flex" justifyContent="space-between" mt={3}>
                  <Button
                    variant="contained"
                    sx={{ bgcolor: "#388E3C", mt: 2 }}
                    onClick={() => editarEgresso()}
                  >
                    {modoEdicao ? "Salvar" : "Criar"}
                  </Button>
                  <Button
                    variant="outlined"
                    sx={{ color: "white", borderColor: "white" }}
                    onClick={() => setPainelAbertoEgresso(false)}
                  >
                    Cancelar
                  </Button>
                </Box>
              </Card>
            )}

            {/* Painel de Listagem de Cursos */}
            {painelListarCursosAberto && cursos.length > 0 && (
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
                <Typography color="black" fontWeight="bold" mb={2}>Lista de Cursos Disponíveis</Typography>

                {cursos.map((curso) => (
                  <Paper
                    key={curso.idCurso}
                    sx={{
                      p: 2,
                      mb: 2,
                      bgcolor: "#4CAF50",
                      color: "white",
                      textAlign: "center",
                    }}
                  >
                    {curso.nome} - {curso.nivel}
                  </Paper>
                ))}

                <Button variant="outlined" sx={{ color: "black", borderColor: "black", mt: 2 }} onClick={fecharPainelListarCursos}>
                  Fechar
                </Button>
              </Card>
            )}
          </Box>
        </Box>

        {/* Depoimento */}
        <Card sx={{ p: 2, mb: 3, borderRadius: 3, bgcolor: "#4CAF50", width: "100%", minHeight: 150 }}>
          <Typography 
            color="white" 
            sx={{ fontFamily: "Courier New, monospace", fontStyle: "italic" }}
          >
            {`"${egresso?.texto || "Depoimento do egresso..."}"`}
          </Typography>
        </Card>
      </Box>
    </Box>
  );
};

export default Home;
