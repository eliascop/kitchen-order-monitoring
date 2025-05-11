package br.com.kitchen.ordermonitoring.app.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/ws/orders/v1/{orderId}")
public class OrderWebSocket {

    private static final Map<String, Set<Session>> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("orderId") String orderId) {
        sessions.computeIfAbsent(orderId, k -> new CopyOnWriteArraySet<>()).add(session);
        System.out.println("Client connected for order: " + orderId);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Received message from client: " + message);
        session.getBasicRemote().sendText("Echo: " + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("orderId") String orderId) {
        Set<Session> orderSessions = sessions.get(orderId);
        if (orderSessions != null) {
            orderSessions.remove(session);
            if (orderSessions.isEmpty()) {
                sessions.remove(orderId);
            }
        }
        System.out.println("Client disconnected from order: " + orderId);
    }

    public static void sendToOrder(String orderId, String message) {
        Set<Session> orderSessions = sessions.get(orderId);
        if (orderSessions != null) {
            for (Session session : orderSessions) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        System.err.println("Erro ao enviar mensagem: " + e.getMessage());
                    }
                }
            }
        }
    }

    public static void notifyOrderUpdate(String orderId, String status) {
        String payload = "{\"orderId\":\"" + orderId + "\", \"status\":\"" + status + "\"}";
        sendToOrder(orderId, payload);
        AllOrdersWebSocket.notifyAll(orderId, status);
    }
}
