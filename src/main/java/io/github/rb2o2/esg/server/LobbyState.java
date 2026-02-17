package io.github.rb2o2.esg.server;

import io.undertow.websockets.core.WebSocketChannel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tracks lobbies by 4-digit code; each lobby holds a pair of client channels.
 */
public final class LobbyState {

    private final ConcurrentHashMap<String, LobbySession> byCode = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WebSocketChannel, String> channelToCode = new ConcurrentHashMap<>();

    public String createLobby(WebSocketChannel channel) {
        String code;
        do {
            code = String.format("%04d", ThreadLocalRandom.current().nextInt(1000, 10000));
        } while (byCode.putIfAbsent(code, new LobbySession(channel)) != null);
        channelToCode.put(channel, code);
        return code;
    }

    public boolean joinLobby(String code, WebSocketChannel channel) {
        LobbySession session = byCode.get(code);
        if (session == null || !session.setPlayer2(channel)) return false;
        channelToCode.put(channel, code);
        return true;
    }

    public WebSocketChannel getPeer(WebSocketChannel channel) {
        String code = channelToCode.get(channel);
        if (code == null) return null;
        LobbySession session = byCode.get(code);
        return session == null ? null : session.getPeer(channel);
    }

    public void removeLobby(WebSocketChannel channel) {
        String code = channelToCode.remove(channel);
        if (code != null) byCode.remove(code);
    }

    private static final class LobbySession {
        private final WebSocketChannel player1;
        private volatile WebSocketChannel player2;

        LobbySession(WebSocketChannel player1) {
            this.player1 = player1;
        }

        synchronized boolean setPlayer2(WebSocketChannel ch) {
            if (player2 != null) return false;
            player2 = ch;
            return true;
        }

        WebSocketChannel getPeer(WebSocketChannel channel) {
            if (channel == player1) return player2;
            if (channel == player2) return player1;
            return null;
        }
    }
}
