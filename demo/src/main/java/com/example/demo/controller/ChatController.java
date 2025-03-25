package com.example.demo.controller;

import com.example.demo.model.Mensagem;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    @MessageMapping("/chat/{grupoId}") // Mensagem recebida do cliente
    @SendTo("/topic/mensagens/{grupoId}") // Mensagem enviada para os participantes
    public Mensagem enviarMensagem(@Payload Mensagem mensagem) {
        mensagem.setDataEnvio(LocalDateTime.now());
        return mensagem; // Envia a mensagem para todos os usuários no grupo
    }
}
