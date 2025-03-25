import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import {
  Box,
  Card,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  AppBar,
  Toolbar,
  IconButton,
} from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import { PieChart, Pie, BarChart, Bar, XAxis, YAxis, Tooltip, Legend } from "recharts";

export default function GraficoCargos() {
  const [tabelaDados, setTabelaDados] = useState([]);
  const [cargoData, setCargoData] = useState([]);
  const [cursoData, setCursoData] = useState([]);
  const navigate = useNavigate();
  // Função para verificar o tipo de usuário no localStorage
  const verificarTipoUsuario = () => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    return usuario ? usuario.tipo : null; // Retorna o tipo de usuário ou null se não houver usuário
  };

  useEffect(() => {
    async function fetchData() {
      try {
        // Buscar TODOS os egressos com cursos
        const cursosResponse = await axios.get("http://localhost:8080/api/curso-egresso/todos");
        // Buscar TODOS os cargos
        const cargosResponse = await axios.get("http://localhost:8080/api/cargos/todos");

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

  return (
    <Box sx={{ width: "50%", padding: 2, margin: "0 auto" }}>
      {/* Barra fixa */}
      <AppBar position="fixed" sx={{ bgcolor: "#4CAF50", width: "50%", left: "25%" }}>
        <Toolbar>
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
          <Typography variant="h6">Gráficos e Tabela de Egressos</Typography>
        </Toolbar>
      </AppBar>

      <Box mt={10}>
        {/* Gráficos */}
        <Box display="flex" justifyContent="space-around" width="100%" mb={3}>
          <PieChart width={300} height={300}>
            <Pie data={cargoData} dataKey="value" cx="50%" cy="50%" outerRadius={100} fill="#82ca9d" label />
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
        <Card sx={{ p: 2, borderRadius: 3, bgcolor: "#4CAF50", width: "100%", minHeight: 150 }}>
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
