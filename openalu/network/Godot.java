package openalu.network;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 *
 * @author ASUS
 */
public class Godot {
    private WebSocket webSocket;

    public void start() {
        try {
            URI uri = URI.create("ws://localhost:9080");
            webSocket = HttpClient.newHttpClient().newWebSocketBuilder().buildAsync(uri, new WebSocket.Listener() {
                    @Override
                    public void onOpen(WebSocket ws) {
                        System.out.println("Connected to Godot WebSocket server!");
                        WebSocket.Listener.super.onOpen(ws);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last) {
                        System.out.println("Received from Godot: " + data);
                        return CompletableFuture.completedFuture(null);
                    }

                    @Override
                    public void onError(WebSocket ws, Throwable error) {
                        System.err.println("WebSocket error: " + error.getMessage());
                        return;
                    }

                    @Override
                    public CompletionStage<?> onClose(WebSocket ws, int statusCode, String reason) {
                        System.out.println("Connection closed: " + statusCode + " (" + reason + ")");
                        return CompletableFuture.completedFuture(null);
                    }
            }).join();

            // Optional initial message
            sendText("Hello from Java!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Send a text message (e.g., simulation data) */
    public void sendText(String message) {
        sendCommand("text", message);
    }
    public void sendCommand(String command, String data) {
        if (webSocket != null) {
            CommandPacket packet = new CommandPacket(command, data);
            webSocket.sendText(packet.parse(), true);
        } else {
            System.err.println("WebSocket is not connected.");
        }
    }
    

    /** Close the WebSocket connection */
    public void close() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Goodbye!")
                      .thenRun(() -> System.out.println("Connection closed."));
        }
    }
}
