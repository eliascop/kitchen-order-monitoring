package br.com.kitchen.ordermonitoring.app.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    private Long id;
    private String name;
    private String description;
    private String type;
    private BigDecimal price;
}
