package com.quaffle.ws.controllers;

import com.quaffle.ws.models.Message;
import com.quaffle.ws.models.OutputMessage;
import com.quaffle.ws.services.RedisOrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/")
public class SocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private RedisOrchestratorService redisOrchestratorService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage processMessageFromClient(@Payload Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage outputMessage = new OutputMessage(message.getFrom(), message.getText(), time);

        redisOrchestratorService.publishMessage(outputMessage);

        return outputMessage;
    }

    @PostMapping
    public void sendMessageToAll(@RequestBody Message message) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        template.convertAndSend("/topic/messages", new OutputMessage(message.getFrom(), message.getText(), time));
    }
}
