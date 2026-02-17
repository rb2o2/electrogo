package io.github.rb2o2.esg.server;

import java.util.Objects;

/**
 * Client-server wire format: one line per message.
 * CONNECT TO LOBBY &lt;code&gt; | CODE &lt;code&gt; | NEW LOBBY | MOVE (x,y,c) | WINNER
 */
public final class GameMessage {

    public enum Type {
        CONNECT_TO_LOBBY,
        CODE,
        NEW_LOBBY,
        MOVE,
        WINNER
    }

    private final Type type;
    private final String payload;

    private GameMessage(Type type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public Type getType() { return type; }
    public String getPayload() { return payload; }

    /** Parse one line into a message, or null if unrecognized. */
    public static GameMessage parse(String line) {
        if (line == null || (line = line.trim()).isEmpty()) return null;
        if ("NEW LOBBY".equals(line)) return new GameMessage(Type.NEW_LOBBY, null);
        if ("WINNER".equals(line)) return new GameMessage(Type.WINNER, null);
        if (line.startsWith("CONNECT TO LOBBY ")) return new GameMessage(Type.CONNECT_TO_LOBBY, line.substring(17).trim());
        if (line.startsWith("CODE ")) return new GameMessage(Type.CODE, line.substring(5).trim());
        if (line.startsWith("MOVE ")) return new GameMessage(Type.MOVE, line.substring(5).trim());
        return null;
    }

    public String toWire() {
        return switch (type) {
            case NEW_LOBBY -> "NEW LOBBY";
            case WINNER -> "WINNER";
            case CONNECT_TO_LOBBY -> "CONNECT TO LOBBY " + Objects.requireNonNull(payload);
            case CODE -> "CODE " + Objects.requireNonNull(payload);
            case MOVE -> "MOVE " + Objects.requireNonNull(payload);
        };
    }

    public static GameMessage newLobby() { return new GameMessage(Type.NEW_LOBBY, null); }
    public static GameMessage winner() { return new GameMessage(Type.WINNER, null); }
    public static GameMessage code(String code) { return new GameMessage(Type.CODE, code); }
    public static GameMessage connectToLobby(String code) { return new GameMessage(Type.CONNECT_TO_LOBBY, code); }
    public static GameMessage move(double x, double y, double c) { return new GameMessage(Type.MOVE, "(" + x + "," + y + "," + c + ")"); }
}
