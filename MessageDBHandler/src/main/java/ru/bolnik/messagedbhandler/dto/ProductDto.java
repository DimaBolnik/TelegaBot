package ru.bolnik.messagedbhandler.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

// DTO для получения данных из Kafka
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long chatId;
    private String type;      // "Bolt" или "Nut"
    private String gost;
    private String size;
    private Integer length;   // только для Bolt
    private Double weight;
}