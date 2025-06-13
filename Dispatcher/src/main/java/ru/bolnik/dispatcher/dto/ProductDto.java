package ru.bolnik.dispatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


// DTO для отправки в Kafka
@Getter
@ToString
@AllArgsConstructor
public class ProductDto {
    private final Long chatId;
    private final String type;
    private final String gost;
    private final String size;
    private final Double weight;
}