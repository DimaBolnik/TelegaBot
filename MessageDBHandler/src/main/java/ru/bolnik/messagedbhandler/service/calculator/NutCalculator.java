package ru.bolnik.messagedbhandler.service.calculator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.dto.ProductDto;
import ru.bolnik.messagedbhandler.dto.ProductResponseDto;
import ru.bolnik.messagedbhandler.service.NutService;
import ru.bolnik.messagedbhandler.service.data.ActionType;
import ru.bolnik.messagedbhandler.service.data.ProductTypeEnum;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NutCalculator implements ProductCalculator {

    private final NutService nutService;

    @Override
    public boolean supports(ProductDto dto) {
        return dto.getType() == ProductTypeEnum.NUT;
    }

    @Override
    public Optional<ProductResponseDto> calculate(ProductDto dto) {
        return nutService.findFirstByGostAndSize(dto.getGost(), dto.getSize())
                .map(n -> {
                    if (dto.getActionType() == ActionType.WEIGHT_TO_QUANTITY) {
                        int quantity = calculateQuantity(dto.getWeight(), n.getWeight());
                        return new ProductResponseDto(dto.getChatId(), dto.getType().toString(), quantity, null);
                    } else if (dto.getActionType() == ActionType.QUANTITY_TO_WEIGHT) {
                        int weight = calculateTotalWeight(dto.getQuantity(), n.getWeight());
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