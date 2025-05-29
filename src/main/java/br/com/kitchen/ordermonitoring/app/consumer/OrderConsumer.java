package br.com.kitchen.ordermonitoring.app.consumer;

import br.com.kitchen.ordermonitoring.app.dto.OrderDTO;
import br.com.kitchen.ordermonitoring.app.websocket.OrderWebSocket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "new-order", groupId = "order-monitoring-consumer-new-order")
    public void listenNewOrder(String strOrder) {
        try {
            OrderDTO orderDTO = objectMapper.readValue(strOrder, OrderDTO.class);
            if(orderDTO.getId() != null && !orderDTO.getStatus().isEmpty()) {
                OrderWebSocket.notifyOrderUpdate(orderDTO);
            }else{
                System.err.println("Invalid message: "+strOrder);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Poorly formatted message: " + strOrder);
        }
    }

    @KafkaListener(topics = "order-status-updates", groupId = "order-monitoring-consumer-order-update")
    public void listenOrderUpdates(String strNewOrderStatus) {
        try{
            OrderDTO orderDTO = objectMapper.readValue(strNewOrderStatus, OrderDTO.class);
            if(orderDTO.getId() != null && !orderDTO.getStatus().isEmpty()) {
                OrderWebSocket.notifyOrderUpdate(orderDTO);
            }else{
                System.err.println("Invalid message: "+strNewOrderStatus);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Poorly formatted message: " + strNewOrderStatus);
        }
    }
}