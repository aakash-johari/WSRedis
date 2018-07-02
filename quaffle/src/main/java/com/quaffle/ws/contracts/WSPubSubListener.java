package com.quaffle.ws.contracts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quaffle.ws.models.OutputMessage;
import com.quaffle.ws.models.RedisMessage;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class WSPubSubListener extends RedisPubSubAdapter<String, String> {
    private SimpMessagingTemplate template;
    private static Logger LOGGER = LoggerFactory.getLogger(WSPubSubListener.class);

    @Autowired
    public WSPubSubListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void message(String channel, String message) {
        ObjectMapper mapper = new ObjectMapper();
        RedisMessage msgObject = null;
        String node = null;

        try {
            node = getNodeName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        LOGGER.info("Message on node {}, channel {} and message {}", node, channel, message);

        try {
            msgObject = mapper.readValue(message, RedisMessage.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        if (!msgObject.getNode().equals(node)) {
            template.convertAndSend("/topic/messages", new OutputMessage(msgObject.getFrom(), msgObject.getText(), msgObject.getTime()));
        }
    }

    private String getNodeName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }
}
