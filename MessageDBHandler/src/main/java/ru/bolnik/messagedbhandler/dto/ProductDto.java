package ru.bolnik.messagedbhandler.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO для получения данных из Kafka
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ProductDto {
    private String type;      // "Bolt" или "Nut"
    private Long chatId;
    private String gost;
    private String size;
    private Integer length;   // только для Bolt
    private Double weight;
}