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
    private final String type; // "Bolt" или "Nut","Washer"
    private final String gost;
    private final Integer size;
    private final Integer length; // Добавил для Bolt
    private final Integer weight;
}