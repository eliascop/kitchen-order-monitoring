package br.com.kitchen.ordermonitoring.app.consumer;

import br.com.kitchen.ordermonitoring.app.model.Order;
import br.com.kitchen.ordermonitoring.app.websocket.OrderWebSocket;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.beans.factory.annotation.Value;

@EnableKafka
public class KafkaConsumer {

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @KafkaListener(topics = "new-order", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderUpdate(Order order) {
         System.out.println("Processando pedido com status: " + order.getStatus());

        if ("PREPARED".equals(order.getStatus())) {
            System.out.println("Pedido pronto: " + order.getId());
            OrderWebSocket.notifyOrderUpdate(order.getId().toString(), order.getStatus());
        }
    }

}
