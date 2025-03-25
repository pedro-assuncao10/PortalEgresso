package com.example.demo.controller;

import com.example.demo.model.Mensagem;
import com.example.demo.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.messaging.simp.SimpMessagingTemplate; // WebSocket
import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;


@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/{grupoId}")
    public ResponseEntity<Mensagem> enviarMensagem(@PathVariable Long grupoId, @Valid @RequestBody Mensagem mensagem) {
        Mensagem novaMensagem = mensagemService.enviarMensagem(grupoId, mensagem);

        // Enviar a mensagem via WebSocket para os participantes do grupo
        // A URL agora terá três segmentos, como exigido
        messagingTemplate.convertAndSend("/topic/grupo/" + grupoId, novaMensagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaMensagem);
    }
    
    @MessageMapping("/grupo/{grupoId}") // Sem "/topic", pois esse é só para envio
    @SendTo("/topic/grupo/{grupoId}")  // Mantém a estrutura correta de destino
    public Mensagem enviarMensagem(@Payload Mensagem mensagem, @DestinationVariable Long grupoId) {
        System.out.println("Mensagem recebida no servidor: " + mensagem.getConteudo());
        return mensagem; // Retorna a mensagem para todos os inscritos no grupo
    }


    @GetMapping("/{grupoId}")
    public ResponseEntity<List<Mensagem>> listarMensagens(@PathVariable Long grupoId) {
        List<Mensagem> mensagens = mensagemService.listarMensagens(grupoId);
        return ResponseEntity.ok(mensagens); // Mesmo se estiver vazio, retorna 200 OK
    }
}
