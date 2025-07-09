package ru.bolnik.messagedbhandler.service.calculator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.service.WasherService;
import ru.bolnik.messagedbhandler.service.data.ProductTypeEnum;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WasherCalculator implements ProductCalculator {

    private final WasherService washerService;

    @Override
    public boolean supports(ProductDto dto) {
        return dto.getType() == ProductTypeEnum.WASHER;
    }

    @Override
    public Optional<ProductResponseDto> calculate(ProductDto dto) {
        return washerService.findFirstByGostAndSize(dto.getGost(), dto.getSize())
                .map(w -> new ProductResponseDto(
                        dto.getChatId(),
                        dto.getType().toString(),
                        calculateQuantity(dto.getWeight(), w.getWeight())));
    }

    private int calculateQuantity(Double totalWeight, Double weightOne) {
        return weightOne > 0 ? (int) (totalWeight / weightOne) : 0;
    }
}