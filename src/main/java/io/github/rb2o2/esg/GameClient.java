package io.github.rb2o2.esg;

import io.github.rb2o2.esg.server.GameMessage;
import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.client.WebSocketClient;
import org.xnio.OptionMap;
import org.xnio.Xnio;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * WebSocket game client. Loads server URL from config.properties; connect then send/receive GameMessages.
 */
public final class GameClient {

    private static final String CONFIG_PATH = "config.properties";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    private final String host;
    private final int port;
    private volatile Consumer<GameMessage> onMessage;
    private volatile Runnable onClose;
    private WebSocketChannel channel;
    private org.xnio.XnioWorker worker;
    private ByteBufferPool bufferPool;

    public GameClient(Runnable onClose) {
        this.onClose = onClose;
        Properties p = loadConfig();
        this.host = p.getProperty("server.host", DEFAULT_HOST);
        this.port = Integer.parseInt(p.getProperty("server.port", String.valueOf(DEFAULT_PORT)));
    }

    public void connect() throws IOException {
        worker = Xnio.getInstance().createWorker(OptionMap.EMPTY);
        bufferPool = new DefaultByteBufferPool(false, 1024);
        URI uri = URI.create("ws://" + host + ":" + port + "/game");
        var future = WebSocketClient.connectionBuilder(worker, bufferPool, uri).connect();
        try {
            channel = future.getInterruptibly();
        } catch (Exception e) {
            throw e instanceof IOException i ? i : new IOException(e);
        }
        channel.getReceiveSetter().set(new AbstractReceiveListener() {
            @Override
            protected void onFullTextMessage(WebSocketChannel ch, BufferedTextMessage message) {
                GameMessage msg = GameMessage.parse(message.getData());
                Consumer<GameMessage> h = onMessage;
                if (msg != null && h != null) SwingUtilities.invokeLater(() -> h.accept(msg));
            }
            @Override
            protected void onCloseMessage(io.undertow.websockets.core.CloseMessage cm, WebSocketChannel ch) {
                Runnable r = onClose;
                if (r != null) SwingUtilities.invokeLater(r);
            }
        });
        channel.resumeReceives();
    }

    public void send(GameMessage msg) {
        if (channel != null && channel.isOpen())
            WebSockets.sendText(msg.toWire(), channel, null);
    }

    public void disconnect() {
        if (channel != null) {
            try { channel.close(); } catch (IOException ignored) { }
            channel = null;
        }
        if (worker != null) { worker.shutdown(); worker = null; }
    }

    public boolean isConnected() { return channel != null && channel.isOpen(); }

    public void setOnMessage(Consumer<GameMessage> handler) { this.onMessage = handler; }
    public void setOnClose(Runnable handler) { this.onClose = handler; }

    private static Properties loadConfig() {
        var p = new Properties();
        Path path = Path.of(CONFIG_PATH);
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) { p.load(in); } catch (IOException ignored) { }
        }
        return p;
    }
}
