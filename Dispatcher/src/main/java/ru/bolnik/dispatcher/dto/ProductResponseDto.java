// путь: ru.bolnik.dispatcher.dto.ProductResponseDto.java

package ru.bolnik.dispatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long chatId;
    private String type; // BOLT, NUT, WASHER
    private Integer quantity; // если считали по весу
    private Integer totalWeight; // если считали по количеству
}
