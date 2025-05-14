package br.com.kitchen.ordermonitoring.app.consumer;

import br.com.kitchen.ordermonitoring.app.dto.OrderDTO;
import br.com.kitchen.ordermonitoring.app.websocket.OrderWebSocket;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    @KafkaListener(topics = "new-order", groupId = "order-monitoring-consumer-new-order")
    public void listenNewOrder(OrderDTO orderDTO) {
        OrderWebSocket.notifyOrderUpdate(orderDTO);
    }

    @KafkaListener(topics = "order-status-updates", groupId = "order-monitoring-consumer-order-update")
    public void listenOrderUpdates(OrderDTO orderDTO) {
        OrderWebSocket.notifyOrderUpdate(orderDTO);
    }
}