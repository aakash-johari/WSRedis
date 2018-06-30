package com.quaffle.ws.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RedisMessage {
    private String node;
    private String from;
    private String text;
    private String time;
}
