package com.quaffle.ws.contracts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quaffle.ws.models.OutputMessage;
import com.quaffle.ws.models.RedisMessage;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class WSPubSubListener extends RedisPubSubAdapter<String, String> {
    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void message(String channel, String message) {
        ObjectMapper mapper = new ObjectMapper();
        RedisMessage msgObject = mapper.convertValue(message, RedisMessage.class);

        try {
            if (!msgObject.getNode().equals(getNodeName())) {
                template.convertAndSend("/topic/messages", new OutputMessage(msgObject.getFrom(), msgObject.getText(), msgObject.getTime()));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private String getNodeName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }
}
