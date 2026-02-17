package io.github.rb2o2.esg.server;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import java.io.IOException;

final class GameMessageReceiver extends AbstractReceiveListener {

    private final LobbyState lobbyState;

    GameMessageReceiver(LobbyState lobbyState, WebSocketChannel channel) {
        this.lobbyState = lobbyState;
    }

    @Override
    protected void onFullTextMessage(WebSocketChannel ch, BufferedTextMessage message) throws IOException {
        GameMessage msg = GameMessage.parse(message.getData());
        if (msg == null) return;
        switch (msg.getType()) {
            case NEW_LOBBY -> {
                String code = lobbyState.createLobby(ch);
                WebSockets.sendText(GameMessage.code(code).toWire(), ch, null);
            }
            case CONNECT_TO_LOBBY -> {
                if (lobbyState.joinLobby(msg.getPayload(), ch))
                    WebSockets.sendText(GameMessage.code(msg.getPayload()).toWire(), ch, null);
            }
            default -> { }
        }
    }
}
