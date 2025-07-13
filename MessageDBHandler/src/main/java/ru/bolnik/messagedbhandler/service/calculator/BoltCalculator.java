package ru.bolnik.messagedbhandler.service.calculator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.service.BoltService;
import ru.bolnik.messagedbhandler.service.data.ActionType;
import ru.bolnik.messagedbhandler.service.data.ProductTypeEnum;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoltCalculator implements ProductCalculator {

    private final BoltService boltService;

    @Override
    public boolean supports(ProductDto dto) {
        return dto.getType() == ProductTypeEnum.BOLT && dto.getLength() != null;
    }

    @Override
    public Optional<ProductResponseDto> calculate(ProductDto dto) {
        return boltService.findFirstByGostAndSizeAndLength(dto.getGost(), dto.getSize(), dto.getLength())
                .map(b -> {
                    if (dto.getActionType() == ActionType.WEIGHT_TO_QUANTITY) {
                        int quantity = calculateQuantity(dto.getWeight(), b.getWeight());
                        return new ProductResponseDto(dto.getChatId(), dto.getType().toString(), quantity, null);
                    } else if (dto.getActionType() == ActionType.QUANTITY_TO_WEIGHT) {
                        int weight = calculateTotalWeight(dto.getQuantity(), b.getWeight());
                        return new ProductResponseDto(dto.getChatId(), dto.getType().toString(), null, weight);
                    }
                    return null;
                });
    }

    private int calculateQuantity(Double totalWeight, Double weightOne) {
        return weightOne > 0 ? (int) (totalWeight / weightOne) : 0;
    }

    private int calculateTotalWeight(Integer quantity, Double weightOne) {
        return weightOne > 0 ? quantity * weightOne.intValue() : 0;
    }
}