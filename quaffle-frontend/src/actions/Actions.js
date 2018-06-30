'use strict';

import Dispatcher from './Dispatcher';
import { ActionTypes, ConnectionStatus } from './ActionTypes';
import SockJS from "sockjs-client"
import Stomp from "@stomp/stompjs"

class Actions {
    constructor() {
        this.socket = new SockJS('http://172.20.20.12:7887/chat');
        this.stompClient = Stomp.over(this.socket);  
    }

    connect() {
        var that = this;

        this.stompClient.connect({}, function(frame) {
            Dispatcher.handleAction({
                type: ActionTypes.CONNECTION_STATUS,
                data: {
                  status: ConnectionStatus.CONNECTED
                }
            });

            console.log('Connected: ' + frame);

            that.stompClient.subscribe('/topic/messages', function(messageOutput) {
                let message = JSON.parse(messageOutput.body);
                Dispatcher.handleAction({
                  type: ActionTypes.SEND_MESSAGE,
                  data: {
                    from: message.from,
                    text: message.text,
                    time: message.time 
                  }
                });
            });
        });
    }

    disconnect() {
        if(stompClient != null) {
            this.stompClient.disconnect();
        }
        Dispatcher.handleAction({
            type: ActionTypes.CONNECTION_STATUS,
            data: {
              status: ConnectionStatus.DISCONNECTED
            }
        });
        console.log("Disconnected");
    }

    sendMessage(from, text) {
        this.stompClient.send("/app/chat", {}, JSON.stringify({ 'from': from, 'text': text }));
    }
};

export default new Actions;
