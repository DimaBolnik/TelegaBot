package ru.bolnik.messagedbhandler.service.calculator;

import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;

import java.util.Optional;

public interface ProductCalculator {
    boolean supports(ProductDto dto);
    Optional<ProductResponseDto> calculate(ProductDto dto);
}