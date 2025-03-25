import { useState } from "react";
import { Box, TextField, Button, Typography, Card } from "@mui/material";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const CadastroEgresso = () => {
  const navigate = useNavigate();
  const [egresso, setEgresso] = useState({
    nome: "",
    email: "",
    descricao: "",
    foto: "",
    linkedin: "",
    instagram: "",
    curriculo: "",
    idCurso: "",
    anoInicio: "",
    anoFim: ""
  });

  const handleChange = (e) => {
    setEgresso({ ...egresso, [e.target.name]: e.target.value });
  };

  const handleSubmit = () => {
    const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));
    if (!usuarioLogado) {
      navigate("/login");
      return;
    }

    axios.post("http://localhost:8080/api/egressos", {
      ...egresso,
      coordenadorId: usuarioLogado.idCoordenador,
    })
    .then(() => {
      navigate("/home");
    })
    .catch(error => {
      console.error("Erro ao cadastrar egresso:", error);
    });
  };

  return (
    <Box display="flex" flexDirection="column" alignItems="center" p={3}>
      <Typography variant="h4" fontWeight="bold" mb={3}>
        Cadastro de Novo Usuário
      </Typography>

      {Object.keys(egresso).map((campo, index) => (
        <Card key={index} sx={{
          width: "600px",
          bgcolor: "#4CAF50",
          borderRadius: "30px",
          p: 2,
          mb: 2
        }}>
          <TextField
            fullWidth
            variant="outlined"
            label={campo.charAt(0).toUpperCase() + campo.slice(1)}
            name={campo}
            value={egresso[campo]}
            onChange={handleChange}
            InputProps={{ sx: { color: "white" } }}
            sx={{
              input: { color: "white" },
              label: { color: "white" },
              "& .MuiOutlinedInput-root": {
                "& fieldset": { borderColor: "white" },
                "&:hover fieldset": { borderColor: "white" },
                "&.Mui-focused fieldset": { borderColor: "white" }
              }
            }}
          />
        </Card>
      ))}

      <Button variant="contained" sx={{ bgcolor: "#2E7D32" }} onClick={handleSubmit}>
        Cadastrar
      </Button>
    </Box>
  );
};

export default CadastroEgresso;
