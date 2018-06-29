'use strict';

import React from 'react';
import MessageStore from '../stores/MessageStore';
import MessageRow from './MessageRow';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Actions from '../actions/Actions';

export default class ChatComponent extends React.Component {
    constructor(props) {
        super(props);

        this._state =
        this.state = {
            messages: [],
            textMessage: "",
            avatarName: "Dud-Bud-Dud"
        };
    }

    componentDidMount() {
        var that = this;

        this.removeListener = MessageStore.addListener(function() {
            that.onMessagesUpdate();
        });

        Actions.connect();
    }

    componentWillUnmount() {
        this.removeListener.remove();
    }

    onMessagesUpdate() {
        this._state.messages = MessageStore.state.messages;

        this.setState(this._state);
    }

    handleChange(name, event) {
        this._state[name] = event.target.value;
        this.setState(this._state);
    }

    onSendMessage() {
        if (this.state.textMessage != null && this.state.textMessage.length > 0) {
            Actions.sendMessage(this.state.avatarName, this.state.textMessage);
            this._state.textMessage = "";
            this.setState(this._state);
        }
    }

    render() {
        let msgs = this.state.messages.map((message, index) => {
            return (
                <MessageRow from={ message.from } message={ message.text } key={ index } />
            );
        });

        return (
            <div>
                <TextField
                  label="Avatar Name"
                  value={ this.state.name }
                  onChange={ (event) => this.handleChange('avatarName', event) }
                  margin="normal"
                  style={ { margin: '10px' } }
                  />
                <TextField
                  id="multiline-flexible"
                  label="Message"
                  multiline
                  rowsMax="4"
                  value={ this.state.textMessage }
                  onChange={ (event) => this.handleChange('textMessage', event) }
                  margin="normal"
                  style={ { margin: '10px', display: 'block', width: '250px' } }
                />
                <Button variant="outlined" color="primary" onClick={ this.onSendMessage.bind(this) }
                        style={ { margin: '10px' } } >Send</Button>
                { msgs }
            </div>
        );
    }
};