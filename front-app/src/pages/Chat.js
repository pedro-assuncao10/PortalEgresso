import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Box, TextField, Button, Typography, IconButton } from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import axios from "axios";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

export default function Chat() {
  const { grupoId } = useParams();
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [idCoordenador, setIdCoordenador] = useState(null);
  const [nomeGrupo, setNomeGrupo] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const [isSending, setIsSending] = useState(false); // Estado para controlar o envio

  useEffect(() => {
    const usuarioLogado = JSON.parse(localStorage.getItem("usuario"));
    if (!usuarioLogado) {
      navigate("/login");
      return;
    }
    setIdCoordenador(usuarioLogado.idCoordenador);
  }, [navigate]);

  const BASE_URL = "https://merry-amazement-production.up.railway.app";

  useEffect(() => {
    if (!grupoId) return;

    // Busca mensagens do grupo e o nome do grupo
    axios
      .get(`${BASE_URL}/api/mensagens/${grupoId}`)
      .then((response) => {
        setMessages(response.data);
      })
      .catch((error) => console.error("Erro ao buscar mensagens:", error));

    axios
      .get(`${BASE_URL}/api/grupos/${grupoId}`)
      .then((response) => setNomeGrupo(response.data.nome))
      .catch((error) => console.error("Erro ao buscar nome do grupo:", error));
  }, [grupoId]);

  useEffect(() => {
    const socket = new SockJS(`${BASE_URL}/ws`);
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
    });

  
    client.onConnect = () => {
      console.log("Conectado ao WebSocket");
  
      client.subscribe(`/topic/grupo/${grupoId}`, (message) => {
        try {
          const novaMensagem = JSON.parse(message.body);
  
          // Verifica se a mensagem já foi adicionada para evitar duplicação
          setMessages((prevMessages) => {
            if (prevMessages.some((msg) => msg.id === novaMensagem.id)) {
              return prevMessages; // Retorna as mensagens atuais sem alteração se já existir
            }
            return [...prevMessages, novaMensagem]; // Adiciona nova mensagem
          });
        } catch (err) {
          console.error("Erro ao processar mensagem do WebSocket:", err);
        }
      });
    };
  
    client.activate();
    setStompClient(client);
  
    return () => {
      client.deactivate();
    };
  }, [grupoId]);
  

  const handleSendMessage = async () => {
    if (newMessage.trim() === "" || !idCoordenador) {
      console.error("Erro: Mensagem vazia ou usuário não autenticado.");
      return;
    }

    setIsSending(true); // Marca que está enviando a mensagem

    const messageData = {
      conteudo: newMessage,
      remetente: { idEgresso: idCoordenador },
    };

    try {
      if (stompClient && stompClient.connected) {
        stompClient.publish({
          destination: `/app/grupo/${grupoId}`, // Ajuste no endpoint
          body: JSON.stringify(messageData),
        });
      }

      // Envia também via HTTP para garantir persistência no banco
      const response = await axios.post(
        `${BASE_URL}/api/mensagens/${grupoId}`,
        messageData
      );

      if (response.status === 201) {
        setMessages((prevMessages) => [...prevMessages, response.data]);
      }
    } catch (err) {
      console.error("Erro ao enviar mensagem:", err);
    }

    setNewMessage("");
    setIsSending(false); // Marca que o envio foi concluído
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        height: "100vh",
        justifyContent: "space-between",
        backgroundColor: "#f5f5f5",
        padding: 2,
      }}
    >
      {/* Cabeçalho com ícone de voltar */}
      <Box
        sx={{
          width: "100%",
          maxWidth: 500,
          backgroundColor: "#1B5E20",
          color: "white",
          padding: 2,
          borderRadius: 2,
          textAlign: "center",
          position: "fixed",
          top: 0,
          zIndex: 1,
        }}
      >
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            gap: 1,
            position: "absolute",
            left: 10,
            top: 10,
          }}
        >
          <IconButton onClick={() => navigate("/grupos")} sx={{ color: "white" }}>
            <ArrowBackIcon />
          </IconButton>
        </Box>
        <Typography variant="h6">
          {nomeGrupo ? `Grupo: ${nomeGrupo}` : "Carregando..."}
        </Typography>
      </Box>

      {/* Área de mensagens */}
      <Box
        sx={{
          marginTop: "80px",
          width: "100%",
          maxWidth: 500,
          flexGrow: 1,
          overflowY: "auto",
          display: "flex",
          flexDirection: "column",
          padding: 2,
          paddingBottom: "80px", // Espaço para o campo de entrada
        }}
      >
        {messages.map((msg, index) => (
          <Box
            key={index}
            sx={{
              alignSelf:
                msg.remetente?.idEgresso === idCoordenador
                  ? "flex-end"
                  : "flex-start",
              backgroundColor:
                msg.remetente?.idEgresso === idCoordenador
                  ? "#4CAF50"
                  : "#66BB6A",
              color: "white",
              padding: 1.5,
              borderRadius: "20px",
              maxWidth: "70%",
              marginBottom: 1,
            }}
          >
            {msg.conteudo}
          </Box>
        ))}
      </Box>

      {/* Campo de entrada de mensagem */}
      <Box
        sx={{
          width: "100%",
          maxWidth: 500,
          display: "flex",
          alignItems: "center",
          gap: 1,
          backgroundColor: "white",
          padding: 1,
          borderRadius: "20px",
          boxShadow: "0px 2px 10px rgba(0,0,0,0.1)",
          position: "fixed",
          bottom: 0,
        }}
      >
        <TextField
          fullWidth
          variant="standard"
          placeholder="Digite sua mensagem..."
          value={newMessage}
          onChange={(e) => setNewMessage(e.target.value)}
          sx={{ paddingLeft: 2 }}
        />
        <Button
          variant="contained"
          sx={{ backgroundColor: "#4CAF50", color: "white", borderRadius: "20px" }}
          onClick={handleSendMessage}
          disabled={isSending} // Desabilita o botão enquanto está enviando
        >
          Enviar
        </Button>
      </Box>
    </Box>
  );
}
