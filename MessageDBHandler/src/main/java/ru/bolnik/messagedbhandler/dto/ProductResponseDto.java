package ru.bolnik.messagedbhandler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO для отправки данных в Kafka
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long chatId;
    private String type; // // "Bolt", "Washer" или "Nut"
    private Integer quantity; // Количество штук
}