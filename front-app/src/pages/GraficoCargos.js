import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Box, Typography, Paper, AppBar, Toolbar, IconButton, Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button } from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { PieChart, Pie, BarChart, Bar, XAxis, YAxis, Tooltip, Legend } from "recharts";

export default function GraficoCargos() {
  const [tabelaDados, setTabelaDados] = useState([]);
  const [cargoData, setCargoData] = useState([]);
  const [cursoData, setCursoData] = useState([]);
  const [depoimentos, setDepoimentos] = useState([]);
  const [depoimentoAtual, setDepoimentoAtual] = useState(null);
  const navigate = useNavigate();
  // Função para verificar o tipo de usuário no localStorage
  const verificarTipoUsuario = () => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    return usuario ? usuario.tipo : null; // Retorna o tipo de usuário ou null se não houver usuário
  };
  const BASE_URL = "https://merry-amazement-production.up.railway.app";

  useEffect(() => {
    async function fetchData() {
      try {
        // Buscar TODOS os egressos com cursos
        const cursosResponse = await axios.get(`${BASE_URL}/api/curso-egresso/todos`);

        // Buscar TODOS os cargos
        const cargosResponse = await axios.get(`${BASE_URL}/api/cargos/todos`);

        // Buscar TODOS os depoimentos
        const depoimentosResponse = await axios.get(`${BASE_URL}/api/depoimentos`);

        setDepoimentos(depoimentosResponse.data);

        if (depoimentosResponse.data.length > 0) {
          setDepoimentoAtual(depoimentosResponse.data[0]); // Primeiro depoimento
        }

        const cursos = cursosResponse.data;
        const cargos = cargosResponse.data;

        const tabelaTemp = [];
        const cargosCount = {};
        const cursosCount = {};

        // Mapear cargos por egresso (usando email como chave para evitar problemas de IDs diferentes)
        const cargosMap = new Map();
        cargos.forEach((cargo) => {
          const email = cargo.egresso.email;
          if (!cargosMap.has(email)) {
            cargosMap.set(email, []);
          }
          cargosMap.get(email).push(cargo.descricao);
          cargosCount[cargo.descricao] = (cargosCount[cargo.descricao] || 0) + 1;
        });

        // Processar dados
        cursos.forEach((cursoEgresso) => {
          const egresso = cursoEgresso.egresso;
          const curso = cursoEgresso.curso;

          // Pegando cargos vinculados pelo email
          const cargosAssociados = cargosMap.get(egresso.email) || ["Nenhum"];

          tabelaTemp.push({
            nome: egresso.nome,
            email: egresso.email,
            nomeCurso: curso.nome,
            nivel: curso.nivel,
            anoInicio: cursoEgresso.anoInicio,
            anoFim: cursoEgresso.anoFim,
            cargoAtual: cargosAssociados.join(", "),
          });

          // Contagem de cursos
          cursosCount[curso.nome] = (cursosCount[curso.nome] || 0) + 1;
        });

        // Atualizar estados
        setTabelaDados(tabelaTemp);
        setCargoData(Object.entries(cargosCount).map(([name, value]) => ({ name, value })));
        setCursoData(Object.entries(cursosCount).map(([name, value]) => ({ name, value })));
      } catch (error) {
        console.error("Erro ao buscar os dados:", error);
      }
    }

    fetchData();
  }, []);

   // Alternar depoimentos dinamicamente
   useEffect(() => {
    if (depoimentos.length === 0) return;

    const interval = setInterval(() => {
      setDepoimentoAtual((prev) => {
        const currentIndex = depoimentos.findIndex((d) => d.idDepoimento === prev?.idDepoimento);
        const nextIndex = (currentIndex + 1) % depoimentos.length;
        return depoimentos[nextIndex];
      });
    }, 5000); // Troca a cada 5 segundos

    return () => clearInterval(interval);
  }, [depoimentos]);

  return (
    <Box sx={{ width: "90%", padding: 2, margin: "0 auto" }}>
      {/* Barra fixa */}
      <AppBar position="fixed" sx={{ bgcolor: "#4CAF50", width: "50%", left: "25%" }}>
        <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
          <Box display="flex" alignItems="center">
            <IconButton
              edge="start"
              color="inherit"
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
            <Typography variant="h6">Página Pública dos Egressos da UFMA</Typography>
          </Box>

          {/* Botão "Faça Login" no canto direito */}
          <Button 
            color="inherit" 
            variant="outlined" 
            sx={{
              borderColor: "white", 
              color: "white", 
              "&:hover": { backgroundColor: "white", color: "#4CAF50" }
            }}
            onClick={() => navigate("/login")}
          >
            Faça Login
          </Button>
        </Toolbar>
      </AppBar>

      {/* Novo balão verde para texto personalizado */}
      <Box 
          display="flex" 
          flexDirection="column" 
          alignItems="center" 
          justifyContent="center"
          width="63%" 
          margin="0 auto"
          mt={12}
      >
        <Paper
          sx={{
            backgroundColor: "#4CAF50",
            color: "white",
            borderRadius: "20px",
            padding: "20px",
            maxWidth: "80%",
            minWidth: "400px",
            textAlign: "left", // Alinhando o texto à esquerda
            fontWeight: "bold",
            lineHeight: "1.6"
          }}
        >
          <Typography variant="h6" gutterBottom>
            📢 Bem-vindo ao Portal de Egressos!
          </Typography>

          <Typography variant="body1" paragraph>
            Nosso sistema foi desenvolvido para conectar egressos, coordenadores e estudantes, 
            promovendo uma rede de informações valiosa sobre trajetórias acadêmicas e profissionais.
          </Typography>

          <Typography variant="body1" paragraph>
            🔹 <strong>Depoimentos Inspiradores:</strong> Leia histórias e experiências compartilhadas 
            pelos egressos sobre suas carreiras, desafios e conquistas.
          </Typography>

          <Typography variant="body1" paragraph>
            📊 <strong>Gráficos Interativos:</strong>
            <br />- <strong>Gráfico de Pizza:</strong> Visualize os principais cargos ocupados pelos egressos.
            <br />- <strong>Gráfico de Barras:</strong> Compare os cursos e a distribuição dos profissionais no mercado.
          </Typography>

          <Typography variant="body1" paragraph>
            📄 <strong>Tabela de Egressos:</strong> Explore uma base de dados com informações detalhadas, 
            incluindo nome, curso, ano de formação e cargo atual.
          </Typography>

          <Typography variant="body1">
            Nosso objetivo é fortalecer a comunidade acadêmica, facilitar conexões e fornecer insights 
            valiosos sobre o futuro profissional dos egressos. 🚀
          </Typography>
        </Paper>
      </Box>


      {/* Balão fixo para depoimentos */}
      {depoimentoAtual && (
        <Box 
          display="flex" 
          flexDirection="column" 
          alignItems="center" 
          justifyContent="center"
          width="50%" 
          margin="0 auto"
          mt={12}
        >
          <Paper
            sx={{
              backgroundColor: "#4CAF50",
              color: "white",
              borderRadius: "20px",
              padding: "10px 20px",
              width: "100%",
              textAlign: "center",
              fontWeight: "bold",
              boxShadow: 3,
            }}
          >
            {depoimentoAtual.egresso.nome} • {depoimentoAtual.egresso.descricao}
          </Paper>
          <Paper
            sx={{
              backgroundColor: "#A5D6A7",
              color: "black",
              borderRadius: "20px",
              padding: "20px",
              marginTop: "10px",
              width: "100%",
              minHeight: "120px",
              textAlign: "justify",
              fontSize: "16px",
              boxShadow: 3,
            }}
          >
            {depoimentoAtual.texto}
          </Paper>
        </Box>
      )}

      <Box mt={10}>
        {/* Gráficos */}
          <Box 
            display="flex" 
            justifyContent="center" 
            alignItems="center" 
            width="100%" 
            mb={3} 
            gap={4} // Reduz a separação entre os gráficos
          >
          <PieChart width={300} height={300}>
            <Pie
              data={cargoData}
              dataKey="value"
              nameKey="name"
              cx="50%"
              cy="50%"
              outerRadius={100}
              fill="#82ca9d"
              label
            />
            <Tooltip />
            <Legend />
          </PieChart>

          <BarChart width={300} height={300} data={cursoData}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="value" fill="#4CAF50" />
          </BarChart>
        </Box>

        {/* Tabela de Egressos */}
        <Card sx={{ p: 2, borderRadius: 3, bgcolor: "#4CAF50", width: "50%", margin: "0 auto", minHeight: 150 }}>
          <Typography color="white" fontWeight="bold" mb={2}>
            Tabela de Egressos
          </Typography>

          <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="tabela de egressos">
              <TableHead>
                <TableRow>
                  <TableCell>Nome</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Curso</TableCell>
                  <TableCell>Nível</TableCell>
                  <TableCell>Ano Início</TableCell>
                  <TableCell>Ano Fim</TableCell>
                  <TableCell>Cargo Atual</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {tabelaDados.map((row, index) => (
                  <TableRow key={index}>
                    <TableCell>{row.nome}</TableCell>
                    <TableCell>{row.email}</TableCell>
                    <TableCell>{row.nomeCurso}</TableCell>
                    <TableCell>{row.nivel}</TableCell>
                    <TableCell>{row.anoInicio}</TableCell>
                    <TableCell>{row.anoFim}</TableCell>
                    <TableCell>{row.cargoAtual}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Card>
      </Box>
    </Box>
  );
}
