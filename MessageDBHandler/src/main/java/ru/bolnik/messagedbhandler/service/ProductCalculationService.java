package ru.bolnik.messagedbhandler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.service.calculator.ProductCalculator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCalculationService {

    private final List<ProductCalculator> calculators;

    public Optional<ProductResponseDto> calculateQuantity(ProductDto dto) {
        return calculators.stream()
                .filter(c -> c.supports(dto))
                .findFirst()
                .flatMap(c -> c.calculate(dto));
    }
}