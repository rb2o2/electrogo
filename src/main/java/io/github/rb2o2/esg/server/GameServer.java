package io.github.rb2o2.esg.server;

import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.WebSocketProtocolHandshakeHandler;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class GameServer {

    private static final String CONFIG_PATH = "config.properties";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    private Undertow server;

    public void start() throws IOException {
        var props = loadConfig();
        String host = props.getProperty("server.host", DEFAULT_HOST);
        int port = Integer.parseInt(props.getProperty("server.port", String.valueOf(DEFAULT_PORT)));

        LobbyState lobbyState = new LobbyState();
        WebSocketConnectionCallback callback = new WebSocketConnectionCallback() {
            @Override
            public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                channel.getReceiveSetter().set(new GameMessageReceiver(lobbyState, channel));
                channel.resumeReceives();
            }
        };
        PathHandler path = new PathHandler()
                .addExactPath("/game", new WebSocketProtocolHandshakeHandler(callback));

        server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(path)
                .build();
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    private static Properties loadConfig() throws IOException {
        var p = new Properties();
        Path path = Path.of(CONFIG_PATH);
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                p.load(in);
            }
        }
        return p;
    }
}
