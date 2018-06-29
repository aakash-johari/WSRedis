'use strict';

import { Store } from "flux/utils";
import Dispatcher from "../actions/Dispatcher";
import { ActionTypes, ConnectionStatus } from "../actions/ActionTypes";

class MessageStore extends Store {
	constructor() {
		super(Dispatcher);

		this.state = {
			messages: [],
			connectionStatus: ConnectionStatus.DISCONNECTED
		}
	}

    __onDispatch(payload) {
    	switch (payload.type) {
    		case ActionTypes.CONNECTION_STATUS:
    			this.state.connectionStatus = payload.data.status;
    		break;

    		case ActionTypes.SEND_MESSAGE:
    			this.state.messages.push(payload.data);
    		break;
    	}

    	this.__emitChange();
    }
};

export default new MessageStore;