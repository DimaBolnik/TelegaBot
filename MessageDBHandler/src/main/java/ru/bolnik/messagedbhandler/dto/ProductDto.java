package ru.bolnik.messagedbhandler.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.bolnik.messagedbhandler.service.data.ProductTypeEnum;

// DTO для получения данных из Kafka
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long chatId;
    private ProductTypeEnum type;      // "Bolt", "Washer" или "Nut"
    private String gost;
    private String size;
    private Integer length;   // только для Bolt
    private Double weight;   // Общий вес изделий
}