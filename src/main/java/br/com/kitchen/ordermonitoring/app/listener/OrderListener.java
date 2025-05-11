package br.com.kitchen.ordermonitoring.app.listener;

import br.com.kitchen.ordermonitoring.app.model.Order;
import br.com.kitchen.ordermonitoring.app.websocket.OrderWebSocket;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    @KafkaListener(topics = "new-order", groupId = "order-monitoring-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(Order order) {
        System.out.println("Received order update: " + order);

        OrderWebSocket.notifyOrderUpdate(order.getId().toString(), order.getStatus());
    }
}