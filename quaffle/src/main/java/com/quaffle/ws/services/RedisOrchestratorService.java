package com.quaffle.ws.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.quaffle.ws.contracts.WSPubSubListener;
import com.quaffle.ws.models.OutputMessage;
import com.quaffle.ws.models.RedisMessage;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class RedisOrchestratorService {

    private RedisClient redisClient;

    private StatefulRedisConnection<String, String> connection;

    private StatefulRedisPubSubConnection<String, String> pubSubConnection;

    private String channelName;

    private RedisPubSubCommands<String, String> pubSubCommands;

    private RedisCommands<String, String> commands;

    @Autowired
    public RedisOrchestratorService(RedisClient redisClient,
                                    @Value("${redisConfig.channel}") String channelName) {
        this.redisClient = redisClient;
        this.channelName = channelName;

        connection = redisClient.connect();
        pubSubConnection = redisClient.connectPubSub();

        pubSubConnection.addListener(new WSPubSubListener());
        pubSubCommands = pubSubConnection.sync();
        pubSubCommands.subscribe(channelName);
        commands = connection.sync();
    }

    public void publishMessage(OutputMessage message) throws JsonProcessingException, UnknownHostException {
        RedisMessage rMessage = new RedisMessage(getNodeName(), message.getFrom(), message.getText(), message.getTime());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String serializedMessage = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rMessage);

        commands.publish(channelName, serializedMessage);
    }

    private String getNodeName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }
}
