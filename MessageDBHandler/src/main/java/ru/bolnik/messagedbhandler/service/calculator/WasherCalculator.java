package ru.bolnik.messagedbhandler.service.calculator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.service.WasherService;
import ru.bolnik.messagedbhandler.service.data.ActionType;
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
                .map(w -> {
                    if (dto.getActionType() == ActionType.WEIGHT_TO_QUANTITY) {
                        int quantity = calculateQuantity(dto.getWeight(), w.getWeight());
                        return new ProductResponseDto(dto.getChatId(), dto.getType().toString(), quantity, null);
                    } else if (dto.getActionType() == ActionType.QUANTITY_TO_WEIGHT) {
                        int weight = calculateTotalWeight(dto.getQuantity(), w.getWeight());
                        return new ProductResponseDto(dto.getChatId(), dto.getType().toString(), null, weight);
                    }
                    return null;
                });
    }

    private int calculateQuantity(Integer totalWeight, Integer weightOne) {
        return weightOne > 0 ? (int) (totalWeight / weightOne) : 0;
    }

    private int calculateTotalWeight(Integer quantity, Integer weightOne) {
        return weightOne > 0 ? quantity * weightOne : 0;
    }
}